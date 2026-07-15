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

private val LANGUAGE_KEY = stringPreferencesKey("language")
private val FONT_SIZE_KEY = stringPreferencesKey("font_size")

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    override fun observeSettings(): Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            language = prefs[LANGUAGE_KEY]?.toEnumOrNull<Language>() ?: Language.VI,
            fontSize = prefs[FONT_SIZE_KEY]?.toEnumOrNull<FontSize>() ?: FontSize.MEDIUM,
        )
    }

    override suspend fun setLanguage(language: Language) {
        dataStore.edit { it[LANGUAGE_KEY] = language.name }
    }

    override suspend fun setFontSize(fontSize: FontSize) {
        dataStore.edit { it[FONT_SIZE_KEY] = fontSize.name }
    }
}

/** Phòng khi giá trị lưu cũ không khớp enum hiện tại (đổi tên hằng số...) → fallback default thay vì crash. */
private inline fun <reified T : Enum<T>> String.toEnumOrNull(): T? =
    runCatching { enumValueOf<T>(this) }.getOrNull()
