package com.example.healthtracker.domain.model

import java.time.LocalDate

data class ActivityEntry(
    val id: Long = 0,

    val activityId: Long,
    val logDate: LocalDate,
    val activityName: String,
    val durationMinutes: Int,
    val caloriesBurned: Double,
)
