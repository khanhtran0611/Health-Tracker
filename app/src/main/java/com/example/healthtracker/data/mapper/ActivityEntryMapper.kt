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

fun ActivityEntry.toEntity(): ActivityEntryEntity = ActivityEntryEntity(
    id = id,
    activityId = activityId,
    logDate = logDate,
    activityName = activityName,
    durationMinutes = durationMinutes,
    caloriesBurned = caloriesBurned,
)
