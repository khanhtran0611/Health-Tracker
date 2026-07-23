package com.example.healthtracker.ui.profile

import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal

enum class BmiCategory {
    UNDERWEIGHT,
    NORMAL,
    OVERWEIGHT,
    OBESE,
}

data class ProfileUiState(
    val isLoading: Boolean = true,
    val fullName: String = "",
    val age: Int? = null,
    val gender: Gender = Gender.MALE,
    val weightKg: Double = 0.0,
    val heightCm: Double = 0.0,
    val activityLevel: Int = 1,
    val goal: Goal = Goal.MAINTAIN,
    val bmi: Double = 0.0,

    val tdee: Double = 0.0,
) {
    val bmiCategory: BmiCategory
        get() = when {
            bmi < 18.5 -> BmiCategory.UNDERWEIGHT
            bmi < 25.0 -> BmiCategory.NORMAL
            bmi < 30.0 -> BmiCategory.OVERWEIGHT
            else -> BmiCategory.OBESE
        }
}
