package com.example.healthtracker.domain.model

import java.time.LocalDate

/**
 * Hồ sơ người dùng (chỉ 1 user). Không chứa BMR/TDEE/BMI/tuổi — tất cả derive
 * runtime từ các trường ở đây. Timestamp created/updated là chi tiết lưu trữ,
 * do repository quản lý nên không xuất hiện ở model.
 */
data class User(
    val id: Long = 0,
    val fullName: String,
    val dateOfBirth: LocalDate,
    val gender: Gender,
    val weightKg: Double,
    val heightCm: Double,
    /** 1..5 → hệ số 1.2 / 1.375 / 1.55 / 1.725 / 1.9 */
    val activityLevel: Int,
    val goal: Goal,
)
