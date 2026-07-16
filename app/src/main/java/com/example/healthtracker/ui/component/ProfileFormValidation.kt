package com.example.healthtracker.ui.component

/** Kiểm tra từng field, gắn lỗi (nếu có) vào state. Gọi lại mỗi khi 1 field đổi. */
fun validateProfileForm(state: ProfileFormUiState): ProfileFormUiState {
    val weight = state.weightKg.toDoubleOrNull()
    val height = state.heightCm.toDoubleOrNull()

    val fullNameError = if (state.fullName.isBlank()) ProfileFieldError.REQUIRED else null

    val dateOfBirthError = if (state.dateOfBirth == null) ProfileFieldError.REQUIRED else null

    val weightError = when {
        state.weightKg.isBlank() -> ProfileFieldError.REQUIRED
        weight == null -> ProfileFieldError.INVALID_NUMBER
        weight <= 0 -> ProfileFieldError.MUST_BE_POSITIVE
        else -> null
    }

    val heightError = when {
        state.heightCm.isBlank() -> ProfileFieldError.REQUIRED
        height == null -> ProfileFieldError.INVALID_NUMBER
        height <= 0 -> ProfileFieldError.MUST_BE_POSITIVE
        else -> null
    }

    return state.copy(
        fullNameError = fullNameError,
        dateOfBirthError = dateOfBirthError,
        weightError = weightError,
        heightError = heightError,
    )
}

/** Form hợp lệ để bấm Save/Next hay chưa — gọi SAU khi đã validateProfileForm. */
fun isProfileFormValid(state: ProfileFormUiState): Boolean {
    return state.fullNameError == null &&
        state.dateOfBirthError == null &&
        state.weightError == null &&
        state.heightError == null
}
