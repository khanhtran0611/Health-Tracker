package com.example.healthtracker.domain.usecase

import javax.inject.Inject

/**
 * BMI = weight(kg) / height(m)². Tính runtime (KHÔNG lưu DB).
 */
class CalculateBmiUseCase @Inject constructor() {
    fun calculate(weightKg: Double, heightCm: Double): Double {
        val heightM = heightCm / 100
        return weightKg / (heightM * heightM)
    }
}
