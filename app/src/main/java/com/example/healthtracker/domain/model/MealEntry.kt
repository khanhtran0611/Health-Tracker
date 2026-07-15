package com.example.healthtracker.domain.model

import java.time.LocalDate

/**
 * Một dòng nhật ký ăn. `foodName` + `calories` là snapshot tại thời điểm log
 * (đã tính sẵn = food.calories × quantity), không đổi khi catalog thay đổi.
 */
data class MealEntry(
    val id: Long = 0,
    /** Luôn trỏ tới 1 Food có thật. */
    val foodId: Long,
    val logDate: LocalDate,
    val mealType: MealType,
    val foodName: String,
    val quantity: Double,
    val calories: Double,
)
