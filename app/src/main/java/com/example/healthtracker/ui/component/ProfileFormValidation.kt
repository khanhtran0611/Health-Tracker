package com.example.healthtracker.ui.component

// Giới hạn hợp lý cho cân nặng/chiều cao con người — chặn ở validate để TDEE/BMI
// tính ra không bị số khủng làm tràn/vỡ layout ở Dashboard, Profile...
private const val MIN_WEIGHT_KG = 25.0
private const val MAX_WEIGHT_KG = 400.0
private const val MIN_HEIGHT_CM = 50.0
private const val MAX_HEIGHT_CM = 250.0

/** Kiểm tra từng field, gắn lỗi (nếu có) vào state. Gọi lại mỗi khi 1 field đổi. */
fun validateProfileForm(state: ProfileFormUiState): ProfileFormUiState {
    val weight = state.weightKg.toDoubleOrNull()
    val height = state.heightCm.toDoubleOrNull()

    val fullNameError = if (state.fullName.isBlank()) FieldError.REQUIRED else null

    val dateOfBirthError = if (state.dateOfBirth == null) FieldError.REQUIRED else null

    val weightError = when {
        state.weightKg.isBlank() -> FieldError.REQUIRED
        weight == null -> FieldError.INVALID_NUMBER
        weight <= 0 -> FieldError.MUST_BE_POSITIVE
        weight < MIN_WEIGHT_KG || weight > MAX_WEIGHT_KG -> FieldError.WEIGHT_OUT_OF_RANGE
        else -> null
    }

    val heightError = when {
        state.heightCm.isBlank() -> FieldError.REQUIRED
        height == null -> FieldError.INVALID_NUMBER
        height <= 0 -> FieldError.MUST_BE_POSITIVE
        height < MIN_HEIGHT_CM || height > MAX_HEIGHT_CM -> FieldError.HEIGHT_OUT_OF_RANGE
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
