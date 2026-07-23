package com.example.healthtracker.data.repository

import com.example.healthtracker.data.di.ApplicationScope
import com.example.healthtracker.data.local.dao.UserDao
import com.example.healthtracker.data.mapper.toDomain
import com.example.healthtracker.data.mapper.toEntity
import com.example.healthtracker.domain.model.User
import com.example.healthtracker.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    @ApplicationScope appScope: CoroutineScope,
) : UserRepository {

    private val userState = userDao.observeUser()
        .map { it?.toDomain() }
        .stateIn(appScope, SharingStarted.Eagerly, null)

    override fun observeUser(): Flow<User?> = userState

    override suspend fun getUser(): User? = userDao.getUser()?.toDomain()

    override suspend fun hasUser(): Boolean = userDao.count() > 0

    override suspend fun saveUser(user: User) {
        val now = System.currentTimeMillis()
        val existing = userDao.getUser()

        val entity = user.toEntity().copy(
            id = existing?.id ?: user.id,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now,
        )
        userDao.upsert(entity)
    }

    override suspend fun deleteUser() = userDao.deleteAll()
}
