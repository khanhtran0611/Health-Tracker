package com.example.healthtracker.ui.meal.enterfood

import com.example.healthtracker.ui.component.formatting.FieldError

fun validateEnterFoodManuallyForm(state: EnterFoodManuallyUiState): EnterFoodManuallyUiState {
    val calories = state.caloriesInput.toDoubleOrNull()

    val nameError = if (state.name.isBlank()) FieldError.REQUIRED else null

    val caloriesError = when {
        state.caloriesInput.isBlank() -> FieldError.REQUIRED
        calories == null -> FieldError.INVALID_NUMBER
        calories <= 0 -> FieldError.MUST_BE_POSITIVE
        else -> null
    }

    return state.copy(nameError = nameError, caloriesError = caloriesError)
}

fun isEnterFoodManuallyFormValid(state: EnterFoodManuallyUiState): Boolean {
    return state.nameError == null && state.caloriesError == null
}
