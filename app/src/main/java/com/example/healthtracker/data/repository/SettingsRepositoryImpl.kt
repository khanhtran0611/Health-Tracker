package com.example.healthtracker.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.healthtracker.domain.model.AppSettings
import com.example.healthtracker.domain.model.Brightness
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.Language
import com.example.healthtracker.domain.model.ThemePreset
import com.example.healthtracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Ngăn kéo lưu trữ các config. Giá trị lưu vào ko phải là string thường mà là tên của enum
private val LANGUAGE_KEY = stringPreferencesKey("language")
private val FONT_SIZE_KEY = stringPreferencesKey("font_size")
private val BRIGHTNESS_KEY = stringPreferencesKey("brightness")
private val THEME_PRESET_KEY = stringPreferencesKey("theme_preset")
private val REMINDERS_ENABLED_KEY = booleanPreferencesKey("reminders_enabled")
private val MORNING_REMINDER_KEY = booleanPreferencesKey("morning_reminder_enabled")
private val NOON_REMINDER_KEY = booleanPreferencesKey("noon_reminder_enabled")
private val EVENING_REMINDER_KEY = booleanPreferencesKey("evening_reminder_enabled")

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    // Ở đây , ta đọc nhiều key cùng một lúc, và lưu gộp vào 1 object là AppSettings
    // Từ đó, để flow collect 1 object cùng lúc thay vì nhiều giá trị rời rạc
    override fun observeSettings(): Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            language = parseLanguage(prefs[LANGUAGE_KEY]),
            fontSize = parseFontSize(prefs[FONT_SIZE_KEY]),
            brightness = parseBrightness(prefs[BRIGHTNESS_KEY]),
            themePreset = parseThemePreset(prefs[THEME_PRESET_KEY]),
            remindersEnabled = prefs[REMINDERS_ENABLED_KEY] ?: false,
            morningReminderEnabled = prefs[MORNING_REMINDER_KEY] ?: true,
            noonReminderEnabled = prefs[NOON_REMINDER_KEY] ?: true,
            eveningReminderEnabled = prefs[EVENING_REMINDER_KEY] ?: true,
        )
    }

    // Lưu giá trị language từ 1 enum
    override suspend fun setLanguage(language: Language) {
        dataStore.edit { it[LANGUAGE_KEY] = language.name }
    }

    override suspend fun setFontSize(fontSize: FontSize) {
        dataStore.edit { it[FONT_SIZE_KEY] = fontSize.name }
    }

    override suspend fun setBrightness(brightness: Brightness) {
        dataStore.edit { it[BRIGHTNESS_KEY] = brightness.name }
    }

    override suspend fun setThemePreset(themePreset: ThemePreset) {
        dataStore.edit { it[THEME_PRESET_KEY] = themePreset.name }
    }

    override suspend fun setRemindersEnabled(enabled: Boolean) {
        dataStore.edit { it[REMINDERS_ENABLED_KEY] = enabled }
    }

    override suspend fun setMorningReminderEnabled(enabled: Boolean) {
        dataStore.edit { it[MORNING_REMINDER_KEY] = enabled }
    }

    override suspend fun setNoonReminderEnabled(enabled: Boolean) {
        dataStore.edit { it[NOON_REMINDER_KEY] = enabled }
    }

    override suspend fun setEveningReminderEnabled(enabled: Boolean) {
        dataStore.edit { it[EVENING_REMINDER_KEY] = enabled }
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

// Đọc giá trị brightness đã lưu ra enum. Không match hoặc chưa lưu lần nào -> default SYSTEM.
private fun parseBrightness(value: String?): Brightness = when (value) {
    "LIGHT" -> Brightness.LIGHT
    "DARK" -> Brightness.DARK
    "SYSTEM" -> Brightness.SYSTEM
    else -> Brightness.SYSTEM
}

// Đọc giá trị themePreset đã lưu ra enum. Không match hoặc chưa lưu lần nào -> default VITALITY_MATERIAL.
private fun parseThemePreset(value: String?): ThemePreset = when (value) {
    "CARBON" -> ThemePreset.CARBON
    "TERRA" -> ThemePreset.TERRA
    "VITALITY_MATERIAL" -> ThemePreset.VITALITY_MATERIAL
    "SILK" -> ThemePreset.SILK
    "CANDY" -> ThemePreset.CANDY
    else -> ThemePreset.VITALITY_MATERIAL
}
