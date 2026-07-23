package com.example.healthtracker.ui.activitydiary.enteractivity

import com.example.healthtracker.ui.component.FieldError

data class EnterActivityUiState(
    val name: String = "",
    val metInput: String = "",
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val nameError: FieldError? = null,
    val metError: FieldError? = null,
)
