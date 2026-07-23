package com.example.healthtracker.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.User
import com.example.healthtracker.domain.repository.ActivityEntryRepository
import com.example.healthtracker.domain.repository.MealEntryRepository
import com.example.healthtracker.domain.repository.UserRepository
import com.example.healthtracker.domain.usecase.CalculateAgeUseCase
import com.example.healthtracker.domain.usecase.CalculateBmrUseCase
import com.example.healthtracker.domain.usecase.CalculateTdeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

private const val STATS_DAYS = 7L

private const val MAX_HISTORY_DAYS = 30L

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mealEntryRepository: MealEntryRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val calculateAgeUseCase: CalculateAgeUseCase,
    private val calculateBmrUseCase: CalculateBmrUseCase,
    private val calculateTdeeUseCase: CalculateTdeeUseCase,
) : ViewModel() {

    private val today: LocalDate = LocalDate.now()

    private val _weekOffset = MutableStateFlow(0)

    private val weeklyFlow: Flow<StatisticsUiState> = _weekOffset
        .flatMapLatest { offset ->
            val endDate = today.minusDays(offset * STATS_DAYS)
            val startDate = endDate.minusDays(STATS_DAYS - 1)

            combine(
                userRepository.observeUser(),
                mealEntryRepository.observeDailyTotalsByDateRange(startDate, endDate),
                activityEntryRepository.observeDailyTotalsByDateRange(startDate, endDate),
            ) { user, eatenByDate, burnedByDate ->
                val tdee = computeTdee(user)
                val dailyStats = (0 until STATS_DAYS).map { dayOffset ->
                    val date = startDate.plusDays(dayOffset)
                    DailyCalorieStat(
                        date = date,
                        eaten = eatenByDate[date] ?: 0.0,
                        burned = burnedByDate[date] ?: 0.0,
                    )
                }
                StatisticsUiState(
                    dailyStats = dailyStats,
                    tdee = tdee,
                    startDate = startDate,
                    endDate = endDate,
                    canGoToPreviousRange = canGoBackFrom(startDate),

                    canGoToNextRange = offset > 0,
                )
            }
        }

    private val monthlyStartDate: LocalDate = today.minusDays(MAX_HISTORY_DAYS - 1)
    private val monthlyFlow: Flow<MonthlySummary> = combine(
        userRepository.observeUser(),
        mealEntryRepository.observeDailyTotalsByDateRange(monthlyStartDate, today),
        activityEntryRepository.observeDailyTotalsByDateRange(monthlyStartDate, today),
    ) { user, eatenByDate, burnedByDate ->
        val tdee = computeTdee(user)
        val stats = (0 until MAX_HISTORY_DAYS).map { dayOffset ->
            val date = monthlyStartDate.plusDays(dayOffset)
            DailyCalorieStat(date = date, eaten = eatenByDate[date] ?: 0.0, burned = burnedByDate[date] ?: 0.0)
        }
        MonthlySummary(
            avgEatenPerDay = stats.sumOf { it.eaten } / stats.size,
            avgBurnedPerDay = stats.sumOf { it.burned } / stats.size,
            daysGoalMet = stats.count { it.isGoalMet(tdee) },
            totalDays = stats.size,
        )
    }

    val uiState: StateFlow<StatisticsUiState> = combine(weeklyFlow, monthlyFlow) { weekly, monthly ->
        weekly.copy(monthlySummary = monthly)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticsUiState())

    private fun computeTdee(user: User?): Double {
        if (user == null) return 0.0
        val age = calculateAgeUseCase.calculate(user.dateOfBirth)
        val bmr = calculateBmrUseCase.calculate(
            gender = user.gender,
            weightKg = user.weightKg,
            heightCm = user.heightCm,
            age = age,
        )
        return calculateTdeeUseCase.calculate(bmr = bmr, activityLevel = user.activityLevel, goal = user.goal)
    }

    private fun canGoBackFrom(startDate: LocalDate): Boolean =
        startDate.minusDays(STATS_DAYS) >= today.minusDays(MAX_HISTORY_DAYS - 1)

    fun onPreviousRange() {
        _weekOffset.update { offset ->
            val endDate = today.minusDays(offset * STATS_DAYS)
            val startDate = endDate.minusDays(STATS_DAYS - 1)

            if (canGoBackFrom(startDate)) offset + 1 else offset
        }
    }

    fun onNextRange() {
        _weekOffset.update { (it - 1).coerceAtLeast(0) }
    }
}
