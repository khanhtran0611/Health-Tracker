package com.example.healthtracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.healthtracker.domain.model.MealType
import java.time.LocalDate

@Entity(
    tableName = "meal_entries",
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["food_id"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["food_id"]),
        Index(value = ["log_date"]),
        Index(value = ["log_date", "meal_type"]),
    ],
)
data class MealEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "food_id")
    val foodId: Long,

    @ColumnInfo(name = "log_date")
    val logDate: LocalDate,

    @ColumnInfo(name = "meal_type")
    val mealType: MealType,

    @ColumnInfo(name = "food_name")
    val foodName: String,

    val quantity: Double = 1.0,

    val calories: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)
