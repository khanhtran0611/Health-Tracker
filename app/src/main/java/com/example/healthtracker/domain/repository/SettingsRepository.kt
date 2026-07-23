package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.AppSettings
import com.example.healthtracker.domain.model.Brightness
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.Language
import com.example.healthtracker.domain.model.ThemePreset
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun setLanguage(language: Language)
    suspend fun setFontSize(fontSize: FontSize)
    suspend fun setBrightness(brightness: Brightness)
    suspend fun setThemePreset(themePreset: ThemePreset)
    suspend fun setRemindersEnabled(enabled: Boolean)
    suspend fun setMorningReminderEnabled(enabled: Boolean)
    suspend fun setNoonReminderEnabled(enabled: Boolean)
    suspend fun setEveningReminderEnabled(enabled: Boolean)

    suspend fun resetToDefaults()
}
