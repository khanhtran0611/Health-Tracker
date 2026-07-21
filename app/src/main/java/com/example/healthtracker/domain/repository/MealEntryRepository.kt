package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.MealEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MealEntryRepository {
    fun observeEntriesByDate(date: LocalDate): Flow<List<MealEntry>>
    /** Tổng calo ăn trong ngày (Eaten cho dashboard). */
    fun observeTotalCaloriesByDate(date: LocalDate): Flow<Double>
    /** Tổng calo ăn theo từng ngày trong khoảng [startDate, endDate] — dùng cho Stats. */
    fun observeDailyTotalsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<Map<LocalDate, Double>>
    suspend fun getEntry(id: Long): MealEntry?
    suspend fun addEntry(entry: MealEntry): Long
    suspend fun updateEntry(entry: MealEntry)
    suspend fun deleteEntry(entry: MealEntry)
    /** Xoá toàn bộ nhật ký bữa ăn — dùng cho tính năng "Đặt lại dữ liệu" ở Settings. */
    suspend fun deleteAllEntries()
}
