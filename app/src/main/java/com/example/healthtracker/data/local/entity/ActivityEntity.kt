package com.example.healthtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Catalog hoạt động (seed sẵn + user tự thêm). Hoạt động user tự nhập được
 * INSERT thẳng vào đây.
 */
@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    /** MET: đi bộ 3.5, chạy 9.8, đạp xe 7.5, bơi 8.0... */
    val met: Double,
)
