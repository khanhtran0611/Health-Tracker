package com.example.healthtracker.domain.usecase

import javax.inject.Inject

class CalculateBmiUseCase @Inject constructor() {
    fun calculate(weightKg: Double, heightCm: Double): Double {
        val heightM = heightCm / 100
        return weightKg / (heightM * heightM)
    }
}
