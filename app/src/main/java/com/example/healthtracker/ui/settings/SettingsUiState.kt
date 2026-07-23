package com.example.healthtracker.ui.settings

import com.example.healthtracker.domain.model.AppSettings

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isResetting: Boolean = false,

    val showAutostartReminderDialog: Boolean = false,
)
