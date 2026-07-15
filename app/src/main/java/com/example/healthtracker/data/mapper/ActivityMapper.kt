package com.example.healthtracker.data.mapper

import com.example.healthtracker.data.local.entity.ActivityEntity
import com.example.healthtracker.domain.model.Activity

fun ActivityEntity.toDomain(): Activity = Activity(
    id = id,
    name = name,
    met = met,
)

fun Activity.toEntity(): ActivityEntity = ActivityEntity(
    id = id,
    name = name,
    met = met,
)
