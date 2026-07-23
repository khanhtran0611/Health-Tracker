package com.example.healthtracker.ui.settings

import com.example.healthtracker.domain.model.AppSettings

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isResetting: Boolean = false,
    /** Hiện dialog nhắc bật Autostart + tắt tối ưu pin — bật lên ngay sau khi user bật công tắc nhắc nhở. */
    val showAutostartReminderDialog: Boolean = false,
)
