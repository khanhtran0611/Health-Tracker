package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(): Flow<User?>
    suspend fun getUser(): User?

    suspend fun hasUser(): Boolean

    suspend fun saveUser(user: User)

    suspend fun deleteUser()
}
