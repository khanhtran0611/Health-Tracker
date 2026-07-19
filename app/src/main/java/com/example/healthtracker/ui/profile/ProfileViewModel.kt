package com.example.healthtracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.repository.UserRepository
import com.example.healthtracker.domain.usecase.CalculateAgeUseCase
import com.example.healthtracker.domain.usecase.CalculateBmiUseCase
import com.example.healthtracker.domain.usecase.CalculateBmrUseCase
import com.example.healthtracker.domain.usecase.CalculateTdeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: UserRepository,
    calculateAgeUseCase: CalculateAgeUseCase,
    calculateBmiUseCase: CalculateBmiUseCase,
    calculateBmrUseCase: CalculateBmrUseCase,
    calculateTdeeUseCase: CalculateTdeeUseCase,
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = userRepository.observeUser()
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileUiState())
}
