package com.example.healthtracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "foods",
    indices = [Index(value = ["name"])],
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val calories: Double,

    @ColumnInfo(name = "serving_unit")
    val servingUnit: String? = null,
)
