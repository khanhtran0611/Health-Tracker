package com.example.healthtracker.ui.component

import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import java.time.LocalDate

data class ProfileFormUiState(
    val fullName: String = "",
    val dateOfBirth: LocalDate? = null,
    val age: Int? = null,
    val gender: Gender = Gender.MALE,
    val weightKg: String = "",
    val heightCm: String = "",
    val activityLevel: Int = 3,
    val goal: Goal = Goal.MAINTAIN,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    val fullNameError: FieldError? = null,
    val dateOfBirthError: FieldError? = null,
    val weightError: FieldError? = null,
    val heightError: FieldError? = null,
)
