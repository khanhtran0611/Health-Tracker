package com.example.healthtracker.data.mapper

import com.example.healthtracker.data.local.entity.UserEntity
import com.example.healthtracker.domain.model.User

fun UserEntity.toDomain(): User = User(
    id = id,
    fullName = fullName,
    dateOfBirth = dateOfBirth,
    gender = gender,
    weightKg = weightKg,
    heightCm = heightCm,
    activityLevel = activityLevel,
    goal = goal,
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    fullName = fullName,
    dateOfBirth = dateOfBirth,
    gender = gender,
    weightKg = weightKg,
    heightCm = heightCm,
    activityLevel = activityLevel,
    goal = goal,
)
