package com.example.healthtracker.ui.mealdiary.enterfood

import com.example.healthtracker.ui.component.FieldError

/**
 * caloriesInput để String (không phải Double) để cho phép gõ dở, giống
 * weightKg/heightCm ở ProfileForm — chỉ parse sang Double lúc validate/submit.
 * foodId (khi sửa) KHÔNG nằm trong state này — chỉ là biến nội bộ của ViewModel
 * (xem AddMealEntryViewModel.mealType — cùng lý do: dữ liệu chỉ dùng lúc Save,
 * không hiển thị lên UI nên không cần trong UiState).
 */
data class EnterFoodManuallyUiState(
    val name: String = "",
    val caloriesInput: String = "",
    val servingUnit: String = "",
    val isSaving: Boolean = false,
    val nameError: FieldError? = null,
    val caloriesError: FieldError? = null,
)
