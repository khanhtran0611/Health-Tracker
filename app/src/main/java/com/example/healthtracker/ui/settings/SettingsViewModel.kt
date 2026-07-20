package com.example.healthtracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.R
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
    private val toastController: ToastController,
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

    fun onResetData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isResetting = true) }
            resetAppDataUseCase.execute()
            toastController.show(ToastMessage(R.string.msg_data_reset, ToastType.SUCCESS))
            _uiState.update { it.copy(isResetting = false) }
            _resetEvent.send(Unit)
        }
    }
}
