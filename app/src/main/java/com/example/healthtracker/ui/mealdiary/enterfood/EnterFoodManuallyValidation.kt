package com.example.healthtracker.ui.mealdiary.enterfood

import com.example.healthtracker.ui.component.FieldError

/** Kiểm tra từng field, gắn lỗi (nếu có) vào state. Gọi lại mỗi khi 1 field đổi. */
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

/** Form hợp lệ để bấm Save hay chưa — gọi SAU khi đã validateEnterFoodManuallyForm. */
fun isEnterFoodManuallyFormValid(state: EnterFoodManuallyUiState): Boolean {
    return state.nameError == null && state.caloriesError == null
}
