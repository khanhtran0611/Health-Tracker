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

    /** Tổng calo ĐỐT theo từng ngày trong khoảng — dùng cho Stats (bar/line chart 7 ngày). */
    @Query(
        "SELECT log_date, COALESCE(SUM(calories_burned), 0) as total FROM activity_entries " +
            "WHERE log_date BETWEEN :startDate AND :endDate GROUP BY log_date"
    )
    fun observeDailyTotalsBetween(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyTotal>>

    @Query("SELECT * FROM activity_entries WHERE id = :id")
    suspend fun getById(id: Long): ActivityEntryEntity?

    @Insert
    suspend fun insert(entry: ActivityEntryEntity): Long

    @Update
    suspend fun update(entry: ActivityEntryEntity)

    @Delete
    suspend fun delete(entry: ActivityEntryEntity)

    @Query("DELETE FROM activity_entries")
    suspend fun deleteAll()
}
