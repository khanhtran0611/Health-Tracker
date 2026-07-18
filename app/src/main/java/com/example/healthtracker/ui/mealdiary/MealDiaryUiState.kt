package com.example.healthtracker.ui.mealdiary

import com.example.healthtracker.domain.model.MealEntry
import com.example.healthtracker.domain.model.MealType
import com.example.healthtracker.ui.navigation.Route
import java.time.LocalDate
import kotlin.collections.emptyList

data class MealDiaryUiState (
    val selectedDate: LocalDate = LocalDate.now(),
    val entriesByMealType : Map<MealType, List<MealEntry>> = MealType.entries.associateWith { emptyList() },
    val totalCaloriesByMealType : Map<MealType, Double> = MealType.entries.associateWith { 0.0 },
    val totalCaloriesToday : Double = 0.0
)