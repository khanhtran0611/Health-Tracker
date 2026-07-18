package com.example.healthtracker.domain.model

import kotlinx.serialization.Serializable

/** Hoạt động trong catalog. */
@Serializable
data class Activity(
    val id: Long = 0,
    val name: String,
    /** MET */
    val met: Double,
)
