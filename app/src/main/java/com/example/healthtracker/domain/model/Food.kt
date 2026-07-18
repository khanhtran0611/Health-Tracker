package com.example.healthtracker.domain.model

/** Món ăn trong catalog. */
data class Food(
    val id: Long = 0,
    val name: String,
    /** kcal / 1 khẩu phần chuẩn */
    val calories: Double,
    /** vd: 100g, 1 tô, 1 quả */
    val servingUnit: String? = null,
)
