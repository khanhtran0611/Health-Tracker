package com.example.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthtracker.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM foods ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%' ORDER BY name COLLATE NOCASE ASC")
    fun search(query: String): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foods WHERE id = :id")
    suspend fun getById(id: Long): FoodEntity?

    @Insert
    suspend fun insert(food: FoodEntity): Long

    @Update
    suspend fun update(food: FoodEntity)

    @Delete
    suspend fun delete(food: FoodEntity)
}
