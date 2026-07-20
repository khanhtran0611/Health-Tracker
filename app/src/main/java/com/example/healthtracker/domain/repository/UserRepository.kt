package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(): Flow<User?>
    suspend fun getUser(): User?
    /** Có hồ sơ chưa — để quyết định vào Onboarding hay Main shell. */
    suspend fun hasUser(): Boolean
    /** Tạo mới hoặc cập nhật hồ sơ (app chỉ 1 user). */
    suspend fun saveUser(user: User)
    /** Xoá hồ sơ user — dùng cho tính năng "Đặt lại dữ liệu" ở Settings. */
    suspend fun deleteUser()
}
