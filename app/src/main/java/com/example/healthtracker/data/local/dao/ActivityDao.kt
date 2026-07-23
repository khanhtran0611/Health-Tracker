package com.example.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthtracker.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE name LIKE '%' || :query || '%' ORDER BY name COLLATE NOCASE ASC")
    fun search(query: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE id = :id")
    suspend fun getById(id: Long): ActivityEntity?

    @Insert
    suspend fun insert(activity: ActivityEntity): Long

    @Update
    suspend fun update(activity: ActivityEntity)

    @Delete
    suspend fun delete(activity: ActivityEntity)
}
