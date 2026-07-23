package com.example.healthtracker.ui.stats

import java.time.LocalDate

data class DailyCalorieStat(
    val date: LocalDate,
    val eaten: Double,
    val burned: Double,
    val toleranceKcal: Double = 50.0
) {

    fun isGoalMet(tdee: Double): Boolean = kotlin.math.abs(tdee - eaten + burned) <= toleranceKcal
}

data class MonthlySummary(
    val avgEatenPerDay: Double = 0.0,
    val avgBurnedPerDay: Double = 0.0,
    val daysGoalMet: Int = 0,
    val totalDays: Int = 30,
)

data class StatisticsUiState(

    val dailyStats: List<DailyCalorieStat> = emptyList(),

    val tdee: Double = 0.0,

    val startDate: LocalDate = LocalDate.now().minusDays(6),
    val endDate: LocalDate = LocalDate.now(),

    val canGoToPreviousRange: Boolean = true,

    val canGoToNextRange: Boolean = false,
    val monthlySummary: MonthlySummary = MonthlySummary(),
) {
    val avgEatenPerDay: Double
        get() = if (dailyStats.isEmpty()) 0.0 else dailyStats.sumOf { it.eaten } / dailyStats.size

    val avgBurnedPerDay: Double
        get() = if (dailyStats.isEmpty()) 0.0 else dailyStats.sumOf { it.burned } / dailyStats.size

    val daysGoalMet: Int
        get() = dailyStats.count { it.isGoalMet(tdee) }
}
