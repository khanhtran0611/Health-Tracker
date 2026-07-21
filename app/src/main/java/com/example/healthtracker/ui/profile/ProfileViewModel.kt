package com.example.healthtracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.repository.UserRepository
import com.example.healthtracker.domain.usecase.CalculateAgeUseCase
import com.example.healthtracker.domain.usecase.CalculateBmiUseCase
import com.example.healthtracker.domain.usecase.CalculateBmrUseCase
import com.example.healthtracker.domain.usecase.CalculateTdeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: UserRepository,
    calculateAgeUseCase: CalculateAgeUseCase,
    calculateBmiUseCase: CalculateBmiUseCase,
    calculateBmrUseCase: CalculateBmrUseCase,
    calculateTdeeUseCase: CalculateTdeeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.observeUser()
                .map { user ->
                    if (user == null) {
                        ProfileUiState(isLoading = false)
                    } else {
                        val age = calculateAgeUseCase.calculate(user.dateOfBirth)
                        val bmr = calculateBmrUseCase.calculate(
                            gender = user.gender,
                            weightKg = user.weightKg,
                            heightCm = user.heightCm,
                            age = age,
                        )
                        ProfileUiState(
                            isLoading = false,
                            fullName = user.fullName,
                            age = age,
                            gender = user.gender,
                            weightKg = user.weightKg,
                            heightCm = user.heightCm,
                            activityLevel = user.activityLevel,
                            goal = user.goal,
                            bmi = calculateBmiUseCase.calculate(weightKg = user.weightKg, heightCm = user.heightCm),
                            tdee = calculateTdeeUseCase.calculate(bmr = bmr, activityLevel = user.activityLevel, goal = user.goal),
                        )
                    }
                }
                .collect { _uiState.value = it }
        }
    }
}
