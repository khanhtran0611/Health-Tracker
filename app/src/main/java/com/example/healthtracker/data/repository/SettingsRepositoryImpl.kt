package com.example.healthtracker.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.healthtracker.domain.model.AppSettings
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.Language
import com.example.healthtracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Ngăn kéo lưu trữ các config. Giá trị lưu vào ko phải là string thường mà là tên của enum
private val LANGUAGE_KEY = stringPreferencesKey("language")
private val FONT_SIZE_KEY = stringPreferencesKey("font_size")

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    // Ở đây , ta đọc nhiều key cùng một lúc, và lưu gộp vào 1 object là AppSettings
    // Từ đó, để flow collect 1 object cùng lúc thay vì nhiều giá trị rời rạc
    override fun observeSettings(): Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            language = parseLanguage(prefs[LANGUAGE_KEY]),
            fontSize = parseFontSize(prefs[FONT_SIZE_KEY]),
        )
    }

    // Lưu giá trị language từ 1 enum
    override suspend fun setLanguage(language: Language) {
        dataStore.edit { it[LANGUAGE_KEY] = language.name }
    }

    override suspend fun setFontSize(fontSize: FontSize) {
        dataStore.edit { it[FONT_SIZE_KEY] = fontSize.name }
    }
}

// Đọc giá trị language đã lưu ra enum. Không match hoặc chưa lưu lần nào -> default VI.
private fun parseLanguage(value: String?): Language = when (value) {
    "EN" -> Language.EN
    "VI" -> Language.VI
    else -> Language.VI
}

// Đọc giá trị fontSize đã lưu ra enum. Không match hoặc chưa lưu lần nào -> default MEDIUM.
private fun parseFontSize(value: String?): FontSize = when (value) {
    "SMALL" -> FontSize.SMALL
    "MEDIUM" -> FontSize.MEDIUM
    "LARGE" -> FontSize.LARGE
    else -> FontSize.MEDIUM
}
