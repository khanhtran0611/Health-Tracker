package com.example.healthtracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import java.time.LocalDate

/**
 * Hồ sơ người dùng. App chỉ có 1 user duy nhất (row id = 1).
 * BMR / TDEE / BMI / tuổi đều derive runtime từ các trường này, KHÔNG lưu.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "full_name")
    val fullName: String,

    @ColumnInfo(name = "date_of_birth")
    val dateOfBirth: LocalDate,

    val gender: Gender,

    @ColumnInfo(name = "weight_kg")
    val weightKg: Double,

    @ColumnInfo(name = "height_cm")
    val heightCm: Double,

    /** 1..5 → hệ số 1.2 / 1.375 / 1.55 / 1.725 / 1.9 */
    @ColumnInfo(name = "activity_level")
    val activityLevel: Int,

    val goal: Goal,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
)
