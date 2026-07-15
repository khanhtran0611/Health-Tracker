package com.example.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthtracker.data.local.entity.ActivityEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ActivityEntryDao {

    @Query("SELECT * FROM activity_entries WHERE log_date = :date ORDER BY created_at ASC")
    fun observeByDate(date: LocalDate): Flow<List<ActivityEntryEntity>>

    /** Tổng calo ĐỐT trong ngày (dùng cho dashboard: Burned). */
    @Query("SELECT COALESCE(SUM(calories_burned), 0) FROM activity_entries WHERE log_date = :date")
    fun observeTotalCaloriesBurnedByDate(date: LocalDate): Flow<Double>

    @Query("SELECT * FROM activity_entries WHERE id = :id")
    suspend fun getById(id: Long): ActivityEntryEntity?

    @Insert
    suspend fun insert(entry: ActivityEntryEntity): Long

    @Update
    suspend fun update(entry: ActivityEntryEntity)

    @Delete
    suspend fun delete(entry: ActivityEntryEntity)
}
