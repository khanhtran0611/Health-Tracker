package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun observeActivities(): Flow<List<Activity>>
    fun searchActivities(query: String): Flow<List<Activity>>
    suspend fun getActivity(id: Long): Activity?

    suspend fun addActivity(activity: Activity): Long
    suspend fun updateActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)
}
