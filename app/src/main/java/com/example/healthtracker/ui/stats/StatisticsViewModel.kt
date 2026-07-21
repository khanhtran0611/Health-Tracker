package com.example.healthtracker.ui.stats

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

private const val STATS_DAYS = 7L

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mealEntryRepository: MealEntryRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val calculateAgeUseCase: CalculateAgeUseCase,
    private val calculateBmrUseCase: CalculateBmrUseCase,
    private val calculateTdeeUseCase: CalculateTdeeUseCase,
) : ViewModel() {

    // Stats không có date picker — luôn xem 7 ngày gần nhất tính đến hôm nay.
    private val today: LocalDate = LocalDate.now()
    private val startDate: LocalDate = today.minusDays(STATS_DAYS - 1)

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepository.observeUser(),
                mealEntryRepository.observeDailyTotalsByDateRange(startDate, today),
                activityEntryRepository.observeDailyTotalsByDateRange(startDate, today),
            ) { user, eatenByDate, burnedByDate ->
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
                val dailyStats = (0 until STATS_DAYS).map { offset ->
                    val date = startDate.plusDays(offset)
                    DailyCalorieStat(
                        date = date,
                        eaten = eatenByDate[date] ?: 0.0,
                        burned = burnedByDate[date] ?: 0.0,
                    )
                }
                StatisticsUiState(dailyStats = dailyStats, tdee = tdee, isLoading = false)
            }.collect { _uiState.value = it }
        }
    }
}
