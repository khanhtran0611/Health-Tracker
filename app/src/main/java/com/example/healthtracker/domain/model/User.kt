package com.example.healthtracker.domain.model

import java.time.LocalDate

data class User(
    val id: Long = 0,
    val fullName: String,
    val dateOfBirth: LocalDate,
    val gender: Gender,
    val weightKg: Double,
    val heightCm: Double,

    val activityLevel: Int,
    val goal: Goal,
)
