package com.example.healthtracker.data.local.dao

import androidx.room.ColumnInfo
import java.time.LocalDate

data class DailyTotal(
    @ColumnInfo(name = "log_date") val logDate: LocalDate,
    @ColumnInfo(name = "total") val total: Double,
)
