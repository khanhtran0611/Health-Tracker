package com.example.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.healthtracker.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    fun observeUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Upsert
    suspend fun upsert(user: UserEntity): Long

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
