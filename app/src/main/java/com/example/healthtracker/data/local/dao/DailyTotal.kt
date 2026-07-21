package com.example.healthtracker.data.local.dao

import androidx.room.ColumnInfo
import java.time.LocalDate

/** Kết quả GROUP BY log_date — dùng chung cho query tổng calo theo ngày của meal/activity entries. */
data class DailyTotal(
    @ColumnInfo(name = "log_date") val logDate: LocalDate,
    @ColumnInfo(name = "total") val total: Double,
)
