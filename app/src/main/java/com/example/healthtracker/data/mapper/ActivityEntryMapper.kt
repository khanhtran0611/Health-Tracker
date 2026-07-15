package com.example.healthtracker.data.mapper

import com.example.healthtracker.data.local.entity.ActivityEntryEntity
import com.example.healthtracker.domain.model.ActivityEntry

fun ActivityEntryEntity.toDomain(): ActivityEntry = ActivityEntry(
    id = id,
    activityId = activityId,
    logDate = logDate,
    activityName = activityName,
    durationMinutes = durationMinutes,
    caloriesBurned = caloriesBurned,
)

/** createdAt để mặc định (now) khi insert; repository giữ createdAt cũ khi update. */
fun ActivityEntry.toEntity(): ActivityEntryEntity = ActivityEntryEntity(
    id = id,
    activityId = activityId,
    logDate = logDate,
    activityName = activityName,
    durationMinutes = durationMinutes,
    caloriesBurned = caloriesBurned,
)
