package com.example.healthtracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Một dòng nhật ký hoạt động. `activityName` + `caloriesBurned` là SNAPSHOT tại
 * thời điểm log. FK activity_id → activities (RESTRICT).
 */
@Entity(
    tableName = "activity_entries",
    foreignKeys = [
        ForeignKey(
            entity = ActivityEntity::class,
            parentColumns = ["id"],
            childColumns = ["activity_id"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["activity_id"]),
        Index(value = ["log_date"]),
    ],
)
data class ActivityEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Luôn trỏ tới 1 row trong activities (NOT NULL). */
    @ColumnInfo(name = "activity_id")
    val activityId: Long,

    @ColumnInfo(name = "log_date")
    val logDate: LocalDate,

    /** Snapshot tên hoạt động lúc log. */
    @ColumnInfo(name = "activity_name")
    val activityName: String,

    @ColumnInfo(name = "duration_minutes")
    val durationMinutes: Int,

    /** Snapshot = met * user.weight_kg * (duration_minutes / 60). */
    @ColumnInfo(name = "calories_burned")
    val caloriesBurned: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)
