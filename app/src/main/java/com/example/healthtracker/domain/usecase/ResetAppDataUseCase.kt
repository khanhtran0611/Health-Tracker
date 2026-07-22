package com.example.healthtracker.domain.usecase

import com.example.healthtracker.domain.repository.ActivityEntryRepository
import com.example.healthtracker.domain.repository.MealEntryRepository
import com.example.healthtracker.domain.repository.SettingsRepository
import com.example.healthtracker.domain.repository.UserRepository
import javax.inject.Inject

/**
 * "Đặt lại dữ liệu" ở Settings — xoá sạch hồ sơ + nhật ký bữa ăn/hoạt động, đồng
 * thời đưa toàn bộ setting (ngôn ngữ, theme, cỡ chữ, nhắc nhở...) về lại default.
 * KHÔNG đụng tới catalog `foods`/`activities` (seed + món/hoạt động tự thêm vẫn giữ).
 */
class ResetAppDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val mealEntryRepository: MealEntryRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend fun execute() {
        mealEntryRepository.deleteAllEntries()
        activityEntryRepository.deleteAllEntries()
        userRepository.deleteUser()
        settingsRepository.resetToDefaults()
    }
}
