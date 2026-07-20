package com.example.healthtracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.Brightness
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.Language
import com.example.healthtracker.domain.model.ThemePreset
import com.example.healthtracker.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

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
}
