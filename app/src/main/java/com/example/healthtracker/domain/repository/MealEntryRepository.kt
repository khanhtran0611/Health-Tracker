package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.MealEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MealEntryRepository {
    fun observeEntriesByDate(date: LocalDate): Flow<List<MealEntry>>
    /** Tổng calo ăn trong ngày (Eaten cho dashboard). */
    fun observeTotalCaloriesByDate(date: LocalDate): Flow<Double>
    suspend fun getEntry(id: Long): MealEntry?
    suspend fun addEntry(entry: MealEntry): Long
    suspend fun updateEntry(entry: MealEntry)
    suspend fun deleteEntry(entry: MealEntry)
}
