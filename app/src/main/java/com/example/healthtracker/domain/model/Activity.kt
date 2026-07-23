package com.example.healthtracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val id: Long = 0,
    val name: String,

    val met: Double,
)
