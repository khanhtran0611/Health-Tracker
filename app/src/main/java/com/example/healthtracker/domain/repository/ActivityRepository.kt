package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun observeActivities(): Flow<List<Activity>>
    fun searchActivities(query: String): Flow<List<Activity>>
    suspend fun getActivity(id: Long): Activity?
    /** Thêm hoạt động mới vào catalog, trả về id để entry trỏ tới. */
    suspend fun addActivity(activity: Activity): Long
    suspend fun updateActivity(activity: Activity)
    /** Ném lỗi RESTRICT nếu hoạt động đang có entry tham chiếu. */
    suspend fun deleteActivity(activity: Activity)
}
