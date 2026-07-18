package com.example.healthtracker.ui.mealdiary.addmealentry

import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.model.MealType
import java.time.LocalDate

data class AddMealEntryUiState(
    /** null = đang load food từ DB (FoodRepository.getFood(foodId)). */
    val food: Food? = null,
    val quantity: Double = 1.0,
    val logDate: LocalDate = LocalDate.now(),
    val mealType: MealType = MealType.BREAKFAST,
    val isSaving: Boolean = false,
) {
    /** Luôn tính lại từ food/quantity hiện tại — không lưu sẵn để tránh bị lệch. */
    val totalCalories: Double
        get() = (food?.calories ?: 0.0) * quantity
}
