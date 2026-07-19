package com.example.healthtracker.domain.usecase

import com.example.healthtracker.domain.model.Gender
import javax.inject.Inject

/**
 * BMR (Nam) = 10×weight + 6.25×height − 5×age + 5
 * BMR (Nữ)  = 10×weight + 6.25×height − 5×age − 161
 * Tính runtime (KHÔNG lưu DB) — dùng làm input cho [CalculateTdeeUseCase].
 */
class CalculateBmrUseCase @Inject constructor() {
    fun calculate(gender: Gender, weightKg: Double, heightCm: Double, age: Int): Double {
        val base = 10 * weightKg + 6.25 * heightCm - 5 * age
        return when (gender) {
            Gender.MALE -> base + 5
            Gender.FEMALE -> base - 161
        }
    }
}
