package com.example.healthtracker.domain.model

import java.time.LocalDate

/**
 * Một dòng nhật ký hoạt động. `activityName` + `caloriesBurned` là snapshot tại
 * thời điểm log.
 */
data class ActivityEntry(
    val id: Long = 0,
    /** Luôn trỏ tới 1 Activity có thật. */
    val activityId: Long,
    val logDate: LocalDate,
    val activityName: String,
    val durationMinutes: Int,
    val caloriesBurned: Double,
)
