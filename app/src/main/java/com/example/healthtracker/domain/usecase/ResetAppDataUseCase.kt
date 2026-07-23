package com.example.healthtracker.domain.usecase

import com.example.healthtracker.domain.repository.ActivityEntryRepository
import com.example.healthtracker.domain.repository.MealEntryRepository
import com.example.healthtracker.domain.repository.SettingsRepository
import com.example.healthtracker.domain.repository.UserRepository
import javax.inject.Inject

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
