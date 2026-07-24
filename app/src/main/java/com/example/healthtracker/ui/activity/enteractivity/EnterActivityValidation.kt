package com.example.healthtracker.ui.activity.enteractivity

import com.example.healthtracker.ui.component.formatting.FieldError

private const val MIN_MET = 0.5
private const val MAX_MET = 20.0

fun validateEnterActivityForm(state: EnterActivityUiState): EnterActivityUiState {
    val met = state.metInput.toDoubleOrNull()

    val nameError = if (state.name.isBlank()) FieldError.REQUIRED else null

    val metError = when {
        state.metInput.isBlank() -> FieldError.REQUIRED
        met == null -> FieldError.INVALID_NUMBER
        met <= 0 -> FieldError.MUST_BE_POSITIVE
        met < MIN_MET || met > MAX_MET -> FieldError.MET_OUT_OF_RANGE
        else -> null
    }

    return state.copy(nameError = nameError, metError = metError)
}

fun isEnterActivityFormValid(state: EnterActivityUiState): Boolean {
    return state.nameError == null && state.metError == null
}
