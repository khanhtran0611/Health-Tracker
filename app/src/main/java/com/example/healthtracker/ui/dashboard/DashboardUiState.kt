package com.example.healthtracker.ui.dashboard

import java.time.LocalDate

enum class CalorieStatus {

    UNDER_TARGET,

    ON_TARGET,

    OVER_TARGET,
}

data class DashboardUiState(
    val today: LocalDate = LocalDate.now(),

    val tdee: Double = 0.0,
    val eatenToday: Double = 0.0,
    val burnedToday: Double = 0.0,
) {

    val remaining: Double
        get() = tdee - eatenToday + burnedToday

    val balance: Double
        get() = eatenToday - burnedToday

    val progress: Float
        get() {
            val effectiveBudget = tdee + burnedToday
            return if (effectiveBudget <= 0.0) 0f else (eatenToday / effectiveBudget).toFloat()
        }

    val calorieStatus: CalorieStatus
        get() = when {
            remaining.toInt() > 0 -> CalorieStatus.UNDER_TARGET
            remaining.toInt() < 0 -> CalorieStatus.OVER_TARGET
            else -> CalorieStatus.ON_TARGET
        }
}
