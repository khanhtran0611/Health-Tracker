package com.example.healthtracker.ui.meal.addmealentry

import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.model.MealType
import java.time.LocalDate

data class AddMealEntryUiState(

    val food: Food? = null,
    val quantity: Double = 1.0,
    val logDate: LocalDate = LocalDate.now(),
    val mealType: MealType = MealType.BREAKFAST,
    val isSaving: Boolean = false,
) {

    val totalCalories: Double
        get() = (food?.calories ?: 0.0) * quantity
}
