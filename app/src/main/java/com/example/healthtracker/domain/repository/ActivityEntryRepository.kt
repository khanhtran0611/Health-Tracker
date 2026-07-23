package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.ActivityEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ActivityEntryRepository {
    fun observeEntriesByDate(date: LocalDate): Flow<List<ActivityEntry>>

    fun observeTotalCaloriesBurnedByDate(date: LocalDate): Flow<Double>

    fun observeDailyTotalsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<Map<LocalDate, Double>>
    suspend fun getEntry(id: Long): ActivityEntry?
    suspend fun addEntry(entry: ActivityEntry): Long
    suspend fun updateEntry(entry: ActivityEntry)
    suspend fun deleteEntry(entry: ActivityEntry)

    suspend fun deleteAllEntries()
}
