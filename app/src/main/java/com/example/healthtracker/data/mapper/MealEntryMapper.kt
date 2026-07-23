package com.example.healthtracker.data.mapper

import com.example.healthtracker.data.local.entity.MealEntryEntity
import com.example.healthtracker.domain.model.MealEntry

fun MealEntryEntity.toDomain(): MealEntry = MealEntry(
    id = id,
    foodId = foodId,
    logDate = logDate,
    mealType = mealType,
    foodName = foodName,
    quantity = quantity,
    calories = calories,
)

fun MealEntry.toEntity(): MealEntryEntity = MealEntryEntity(
    id = id,
    foodId = foodId,
    logDate = logDate,
    mealType = mealType,
    foodName = foodName,
    quantity = quantity,
    calories = calories,
)
