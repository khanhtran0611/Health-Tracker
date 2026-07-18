package com.example.healthtracker.ui.activitydiary.activitypicker

import com.example.healthtracker.domain.model.Activity

data class ActivityPickerUiState(
    val searchQuery: String = "",
    val activities: List<Activity> = emptyList(),
)
