package com.example.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthtracker.data.local.entity.MealEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MealEntryDao {

    @Query("SELECT * FROM meal_entries WHERE log_date = :date ORDER BY created_at ASC")
    fun observeByDate(date: LocalDate): Flow<List<MealEntryEntity>>

    /** Tổng calo ĂN trong ngày (dùng cho dashboard: Eaten). */
    @Query("SELECT COALESCE(SUM(calories), 0) FROM meal_entries WHERE log_date = :date")
    fun observeTotalCaloriesByDate(date: LocalDate): Flow<Double>

    @Query("SELECT * FROM meal_entries WHERE id = :id")
    suspend fun getById(id: Long): MealEntryEntity?

    @Insert
    suspend fun insert(entry: MealEntryEntity): Long

    @Update
    suspend fun update(entry: MealEntryEntity)

    @Delete
    suspend fun delete(entry: MealEntryEntity)
}
