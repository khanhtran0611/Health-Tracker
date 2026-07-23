package com.example.healthtracker.ui.activitydiary.enteractivity

import com.example.healthtracker.ui.component.FieldError

fun validateEnterActivityForm(state: EnterActivityUiState): EnterActivityUiState {
    val met = state.metInput.toDoubleOrNull()

    val nameError = if (state.name.isBlank()) FieldError.REQUIRED else null

    val metError = when {
        state.metInput.isBlank() -> FieldError.REQUIRED
        met == null -> FieldError.INVALID_NUMBER
        met <= 0 -> FieldError.MUST_BE_POSITIVE
        else -> null
    }

    return state.copy(nameError = nameError, metError = metError)
}

fun isEnterActivityFormValid(state: EnterActivityUiState): Boolean {
    return state.nameError == null && state.metError == null
}
