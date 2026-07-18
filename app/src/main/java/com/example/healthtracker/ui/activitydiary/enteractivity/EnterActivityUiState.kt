package com.example.healthtracker.ui.activitydiary.enteractivity

import com.example.healthtracker.ui.component.FieldError

/**
 * metInput để String (không phải Double) để cho phép gõ dở — chỉ parse sang
 * Double lúc validate/submit, giống caloriesInput bên EnterFoodManuallyUiState.
 * activityId (khi sửa) KHÔNG nằm trong state này — chỉ là biến nội bộ của
 * ViewModel, cùng lý do như foodId bên EnterFoodManuallyViewModel.
 */
data class EnterActivityUiState(
    val name: String = "",
    val metInput: String = "",
    val isSaving: Boolean = false,
    val nameError: FieldError? = null,
    val metError: FieldError? = null,
)
