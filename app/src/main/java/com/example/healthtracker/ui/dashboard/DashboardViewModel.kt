package com.example.healthtracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.repository.ActivityEntryRepository
import com.example.healthtracker.domain.repository.MealEntryRepository
import com.example.healthtracker.domain.repository.UserRepository
import com.example.healthtracker.domain.usecase.CalculateAgeUseCase
import com.example.healthtracker.domain.usecase.CalculateBmrUseCase
import com.example.healthtracker.domain.usecase.CalculateTdeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mealEntryRepository: MealEntryRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val calculateAgeUseCase: CalculateAgeUseCase,
    private val calculateBmrUseCase: CalculateBmrUseCase,
    private val calculateTdeeUseCase: CalculateTdeeUseCase,
) : ViewModel() {

    // Dashboard chỉ hiện hôm nay, không có date picker như Meal/Activity Diary.
    private val today: LocalDate = LocalDate.now()

    private val _uiState = MutableStateFlow(DashboardUiState(today = today))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepository.observeUser(),
                mealEntryRepository.observeTotalCaloriesByDate(today),
                activityEntryRepository.observeTotalCaloriesBurnedByDate(today),
            ) { user, eatenToday, burnedToday ->
                val tdee = if (user == null) {
                    0.0
                } else {
                    val age = calculateAgeUseCase.calculate(user.dateOfBirth)
                    val bmr = calculateBmrUseCase.calculate(
                        gender = user.gender,
                        weightKg = user.weightKg,
                        heightCm = user.heightCm,
                        age = age,
                    )
                    calculateTdeeUseCase.calculate(bmr = bmr, activityLevel = user.activityLevel, goal = user.goal)
                }
                DashboardUiState(today = today, tdee = tdee, eatenToday = eatenToday, burnedToday = burnedToday)
            }.collect { _uiState.value = it }
        }
    }
}
