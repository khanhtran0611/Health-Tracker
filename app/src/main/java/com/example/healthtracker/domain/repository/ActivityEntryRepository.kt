package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.ActivityEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ActivityEntryRepository {
    fun observeEntriesByDate(date: LocalDate): Flow<List<ActivityEntry>>
    /** Tổng calo đốt trong ngày (Burned cho dashboard). */
    fun observeTotalCaloriesBurnedByDate(date: LocalDate): Flow<Double>
    suspend fun getEntry(id: Long): ActivityEntry?
    suspend fun addEntry(entry: ActivityEntry): Long
    suspend fun updateEntry(entry: ActivityEntry)
    suspend fun deleteEntry(entry: ActivityEntry)
    /** Xoá toàn bộ nhật ký hoạt động — dùng cho tính năng "Đặt lại dữ liệu" ở Settings. */
    suspend fun deleteAllEntries()
}
