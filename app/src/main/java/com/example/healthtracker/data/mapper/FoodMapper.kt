package com.example.healthtracker.data.mapper

import com.example.healthtracker.data.local.entity.FoodEntity
import com.example.healthtracker.domain.model.Food

fun FoodEntity.toDomain(): Food = Food(
    id = id,
    name = name,
    calories = calories,
    servingUnit = servingUnit,
)

fun Food.toEntity(): FoodEntity = FoodEntity(
    id = id,
    name = name,
    calories = calories,
    servingUnit = servingUnit,
)
