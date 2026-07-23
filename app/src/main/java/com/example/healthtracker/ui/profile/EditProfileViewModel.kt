package com.example.healthtracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import com.example.healthtracker.domain.model.User
import com.example.healthtracker.domain.repository.UserRepository
import com.example.healthtracker.domain.usecase.CalculateAgeUseCase
import com.example.healthtracker.ui.component.profileform.ProfileFormUiState
import com.example.healthtracker.ui.component.profileform.isProfileFormValid
import com.example.healthtracker.ui.component.profileform.validateProfileForm
import com.example.healthtracker.ui.toast.ToastController
import com.example.healthtracker.ui.toast.ToastMessage
import com.example.healthtracker.ui.toast.ToastType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val calculateAgeUseCase: CalculateAgeUseCase,
    private val toastController: ToastController,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileFormUiState(isLoading = true))
    val uiState: StateFlow<ProfileFormUiState> = _uiState.asStateFlow()

    private val _savedEvent = Channel<Unit>(Channel.BUFFERED)
    val savedEvent: Flow<Unit> = _savedEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val user = userRepository.getUser()
            _uiState.value = if (user != null) {
                ProfileFormUiState(
                    fullName = user.fullName,
                    dateOfBirth = user.dateOfBirth,
                    age = calculateAgeUseCase.calculate(user.dateOfBirth),
                    gender = user.gender,
                    weightKg = user.weightKg.toString(),
                    heightCm = user.heightCm.toString(),
                    activityLevel = user.activityLevel,
                    goal = user.goal,
                    isLoading = false,
                )
            } else {
                _uiState.value.copy(isLoading = false)
            }
        }
    }

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

    fun onSubmit() {
        val validated = validateProfileForm(_uiState.value)
        _uiState.value = validated
        if (!isProfileFormValid(validated)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
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
                toastController.show(ToastMessage(R.string.msg_profile_saved, ToastType.SUCCESS))
                _uiState.update { it.copy(isSaving = false) }
                _savedEvent.send(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                toastController.show(ToastMessage(R.string.msg_profile_save_failed, ToastType.ERROR))
                _uiState.update { it.copy(isSaving = false) }

            }
        }
    }
}
