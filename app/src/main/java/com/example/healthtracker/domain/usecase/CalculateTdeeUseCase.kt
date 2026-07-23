package com.example.healthtracker.domain.usecase

import com.example.healthtracker.domain.model.Goal
import javax.inject.Inject

class CalculateTdeeUseCase @Inject constructor() {
    fun calculate(bmr: Double, activityLevel: Int, goal: Goal): Double {
        val activityFactor = when (activityLevel) {
            1 -> 1.2
            2 -> 1.375
            3 -> 1.55
            4 -> 1.725
            5 -> 1.9
            else -> 1.2
        }
        val goalAdjustment = when (goal) {
            Goal.LOSE -> -500.0
            Goal.MAINTAIN -> 0.0
            Goal.GAIN -> 500.0
        }
        return bmr * activityFactor + goalAdjustment
    }
}
