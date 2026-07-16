package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.AppSettings
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.Language
import kotlinx.coroutines.flow.Flow


// Khai báo thế này để đảm bảo pattern chuẩn:
// ViewModel chỉ cần biết đến repo qua interface, ko cần biết chi tiết về data store làm cái gì

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun setLanguage(language: Language)
    suspend fun setFontSize(fontSize: FontSize)
}
