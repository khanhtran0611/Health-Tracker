package com.example.healthtracker.ui.component

import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import java.time.LocalDate

/**
 * UiState dùng chung cho Onboarding và Edit Profile (2 màn hình cùng form).
 * weightKg/heightCm để String để cho phép người dùng gõ dở mà không phải ép kiểu
 * ngay; chỉ parse sang Double lúc validate/submit.
 */
data class ProfileFormUiState(
    val fullName: String = "",
    val dateOfBirth: LocalDate? = null,
    val age: Int? = null, // derived, KHÔNG do user nhập — ViewModel tự tính lại khi dateOfBirth đổi
    val gender: Gender = Gender.MALE,
    val weightKg: String = "",
    val heightCm: String = "",
    val activityLevel: Int = 3, // mặc định Moderate
    val goal: Goal = Goal.MAINTAIN,

    val isLoading: Boolean = false, // Edit Profile: đang tải hồ sơ cũ
    val isSaving: Boolean = false,

    val fullNameError: FieldError? = null,
    val dateOfBirthError: FieldError? = null,
    val weightError: FieldError? = null,
    val heightError: FieldError? = null,
)
