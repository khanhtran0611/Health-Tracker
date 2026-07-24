package com.example.healthtracker.domain.model

import java.time.LocalDate

data class MealEntry(
    val id: Long = 0,

    val foodId: Long,
    val logDate: LocalDate,
    val mealType: MealType,
    val foodName: String,
    val quantity: Int,
    val calories: Double,
)
