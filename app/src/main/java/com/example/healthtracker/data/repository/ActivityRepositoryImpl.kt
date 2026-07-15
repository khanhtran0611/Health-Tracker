package com.example.healthtracker.data.repository

import com.example.healthtracker.data.local.dao.ActivityDao
import com.example.healthtracker.data.mapper.toDomain
import com.example.healthtracker.data.mapper.toEntity
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao,
) : ActivityRepository {

    override fun observeActivities(): Flow<List<Activity>> =
        activityDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun searchActivities(query: String): Flow<List<Activity>> =
        activityDao.search(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getActivity(id: Long): Activity? =
        activityDao.getById(id)?.toDomain()

    override suspend fun addActivity(activity: Activity): Long =
        activityDao.insert(activity.toEntity())

    override suspend fun updateActivity(activity: Activity) =
        activityDao.update(activity.toEntity())

    override suspend fun deleteActivity(activity: Activity) =
        activityDao.delete(activity.toEntity())
}
