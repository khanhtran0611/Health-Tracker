package com.example.healthtracker.ui.activitydiary.addactivityentry

import com.example.healthtracker.domain.model.Activity
import java.time.LocalDate

data class AddActivityEntryUiState(

    val activity: Activity? = null,
    val durationMinutes: Int = 30,
    val logDate: LocalDate = LocalDate.now(),

    val weightKg: Double = 0.0,
    val isSaving: Boolean = false,
) {

    val caloriesBurned: Double
        get() = (activity?.met ?: 0.0) * weightKg * (durationMinutes / 60.0)
}
