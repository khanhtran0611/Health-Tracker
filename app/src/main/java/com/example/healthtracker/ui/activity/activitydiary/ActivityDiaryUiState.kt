package com.example.healthtracker.ui.activity.activitydiary

import com.example.healthtracker.domain.model.ActivityEntry
import java.time.LocalDate

data class ActivityDiaryUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val entries: List<ActivityEntry> = emptyList(),
    val totalCaloriesBurnedToday: Double = 0.0,
    val isLoading: Boolean = true,
)
