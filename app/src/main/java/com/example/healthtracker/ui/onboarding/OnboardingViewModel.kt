package com.example.healthtracker.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import com.example.healthtracker.domain.model.Language
import com.example.healthtracker.domain.model.User
import com.example.healthtracker.domain.repository.SettingsRepository
import com.example.healthtracker.domain.repository.UserRepository
import com.example.healthtracker.domain.usecase.CalculateAgeUseCase
import com.example.healthtracker.ui.component.profileform.ProfileFormUiState
import com.example.healthtracker.ui.component.profileform.isProfileFormValid
import com.example.healthtracker.ui.component.profileform.validateProfileForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val calculateAgeUseCase: CalculateAgeUseCase,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileFormUiState())
    val uiState: StateFlow<ProfileFormUiState> = _uiState.asStateFlow()

    val language: StateFlow<Language> = settingsRepository.observeSettings()
        .map { it.language }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Language.VI)

    private val _completedEvent = Channel<Unit>(Channel.BUFFERED)
    val completedEvent: Flow<Unit> = _completedEvent.receiveAsFlow()

    fun onFullNameChange(value: String) {
        _uiState.update { validateProfileForm(it.copy(fullName = value)) }
    }

    fun onDateOfBirthChange(value: LocalDate) {
        _uiState.update {
            validateProfileForm(it.copy(dateOfBirth = value, age = calculateAgeUseCase.calculate(value)))
        }
    }

    fun onGenderChange(value: Gender) {
        _uiState.update { it.copy(gender = value) }
    }

    fun onWeightChange(value: String) {
        _uiState.update { validateProfileForm(it.copy(weightKg = value)) }
    }

    fun onHeightChange(value: String) {
        _uiState.update { validateProfileForm(it.copy(heightCm = value)) }
    }

    fun onActivityLevelChange(value: Int) {
        _uiState.update { it.copy(activityLevel = value) }
    }

    fun onGoalChange(value: Goal) {
        _uiState.update { it.copy(goal = value) }
    }

    fun onToggleLanguage() {
        viewModelScope.launch {
            val next = if (language.value == Language.VI) Language.EN else Language.VI
            settingsRepository.setLanguage(next)
        }
    }

    fun onSubmit() {
        val validated = validateProfileForm(_uiState.value)
        _uiState.value = validated
        if (!isProfileFormValid(validated)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            userRepository.saveUser(
                User(
                    fullName = validated.fullName,
                    dateOfBirth = validated.dateOfBirth!!,
                    gender = validated.gender,
                    weightKg = validated.weightKg.toDouble(),
                    heightCm = validated.heightCm.toDouble(),
                    activityLevel = validated.activityLevel,
                    goal = validated.goal,
                ),
            )
            _uiState.update { it.copy(isSaving = false) }
            _completedEvent.send(Unit)
        }
    }
}
