package com.example.healthtracker.domain.model

enum class Language {
    VI,
    EN,
}

enum class FontSize {
    SMALL,
    MEDIUM,
    LARGE,
}

data class AppSettings(
    val language: Language = Language.VI,
    val fontSize: FontSize = FontSize.MEDIUM,
    val brightness: Brightness = Brightness.SYSTEM,
    val themePreset: ThemePreset = ThemePreset.VITALITY_MATERIAL,

    val remindersEnabled: Boolean = false,
    val morningReminderEnabled: Boolean = true,
    val noonReminderEnabled: Boolean = true,
    val eveningReminderEnabled: Boolean = true,
)
