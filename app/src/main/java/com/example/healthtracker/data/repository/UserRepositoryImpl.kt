package com.example.healthtracker.data.repository

import com.example.healthtracker.data.local.dao.UserDao
import com.example.healthtracker.data.mapper.toDomain
import com.example.healthtracker.data.mapper.toEntity
import com.example.healthtracker.domain.model.User
import com.example.healthtracker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
) : UserRepository {

    override fun observeUser(): Flow<User?> =
        userDao.observeUser().map { it?.toDomain() }

    override suspend fun getUser(): User? = userDao.getUser()?.toDomain()

    override suspend fun hasUser(): Boolean = userDao.count() > 0

    override suspend fun saveUser(user: User) {
        val now = System.currentTimeMillis()
        val existing = userDao.getUser()
        // Giữ nguyên createdAt của row cũ, chỉ bump updatedAt.
        val entity = user.toEntity().copy(
            id = existing?.id ?: user.id,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now,
        )
        userDao.upsert(entity)
    }
}
