package com.example.healthtracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: Long = 0,
    val name: String,

    val calories: Double,

    val servingUnit: String? = null,
)
