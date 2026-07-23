package com.example.healthtracker.ui.meal.enterfood

import com.example.healthtracker.ui.component.formatting.FieldError

data class EnterFoodManuallyUiState(
    val name: String = "",
    val caloriesInput: String = "",
    val servingUnit: String = "",
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val nameError: FieldError? = null,
    val caloriesError: FieldError? = null,
)
