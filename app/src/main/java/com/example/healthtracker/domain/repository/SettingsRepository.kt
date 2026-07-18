package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.AppSettings
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.Language
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun setLanguage(language: Language)
    suspend fun setFontSize(fontSize: FontSize)
}
