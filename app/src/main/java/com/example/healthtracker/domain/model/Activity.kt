package com.example.healthtracker.domain.model

/** Hoạt động trong catalog. */
data class Activity(
    val id: Long = 0,
    val name: String,
    /** MET */
    val met: Double,
)
