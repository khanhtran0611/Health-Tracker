package com.example.healthtracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Catalog món ăn (seed sẵn + user tự thêm). Món user tự nhập được INSERT thẳng
 * vào đây, không phân biệt seed vs custom.
 */
@Entity(
    tableName = "foods",
    indices = [Index(value = ["name"])],
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    /** kcal / 1 khẩu phần chuẩn */
    val calories: Double,

    /** vd: 100g, 1 tô, 1 quả, 1 ổ bánh mì */
    @ColumnInfo(name = "serving_unit")
    val servingUnit: String? = null,
)
