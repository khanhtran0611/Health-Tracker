package com.example.healthtracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.R
import com.example.healthtracker.core.reminder.ReminderScheduler
import com.example.healthtracker.domain.model.Brightness
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.Language
import com.example.healthtracker.domain.model.ThemePreset
import com.example.healthtracker.domain.repository.SettingsRepository
import com.example.healthtracker.domain.usecase.ResetAppDataUseCase
import com.example.healthtracker.ui.toast.ToastController
import com.example.healthtracker.ui.toast.ToastMessage
import com.example.healthtracker.ui.toast.ToastType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val resetAppDataUseCase: ResetAppDataUseCase,
    private val reminderScheduler: ReminderScheduler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    // One-shot event: đặt lại dữ liệu xong. UI tự quyết định điều hướng về Onboarding.
    private val _resetEvent = Channel<Unit>(Channel.BUFFERED)
    val resetEvent: Flow<Unit> = _resetEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            settingsRepository.observeSettings().collect { settings ->
                _uiState.value = _uiState.value.copy(settings = settings)
            }
        }
    }

    fun onLanguageChange(language: Language) {
        viewModelScope.launch { settingsRepository.setLanguage(language) }
    }

    fun onFontSizeChange(fontSize: FontSize) {
        viewModelScope.launch { settingsRepository.setFontSize(fontSize) }
    }

    fun onBrightnessChange(brightness: Brightness) {
        viewModelScope.launch { settingsRepository.setBrightness(brightness) }
    }

    fun onThemePresetChange(themePreset: ThemePreset) {
        viewModelScope.launch { settingsRepository.setThemePreset(themePreset) }
    }

    fun onRemindersEnabledChange(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setRemindersEnabled(enabled)
            reminderScheduler.rescheduleAll(_uiState.value.settings.copy(remindersEnabled = enabled))
        }
        // Bật nhắc nhở -> nhắc thêm user tự cấp Autostart + bỏ tối ưu pin cho app,
        // không thì AlarmManager dễ bị hệ thống chặn âm thầm trên nhiều máy Android.
        if (enabled) {
            _uiState.update { it.copy(showAutostartReminderDialog = true) }
        }
    }

    fun onAutostartReminderDialogDismissed() {
        _uiState.update { it.copy(showAutostartReminderDialog = false) }
    }

    fun onMorningReminderChange(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setMorningReminderEnabled(enabled)
            reminderScheduler.rescheduleAll(_uiState.value.settings.copy(morningReminderEnabled = enabled))
        }
    }

    fun onNoonReminderChange(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNoonReminderEnabled(enabled)
            reminderScheduler.rescheduleAll(_uiState.value.settings.copy(noonReminderEnabled = enabled))
        }
    }

    fun onEveningReminderChange(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setEveningReminderEnabled(enabled)
            reminderScheduler.rescheduleAll(_uiState.value.settings.copy(eveningReminderEnabled = enabled))
        }
    }

    fun onResetData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isResetting = true) }
            resetAppDataUseCase.execute()
            _uiState.update { it.copy(isResetting = false) }
            _resetEvent.send(Unit)
        }
    }
}
