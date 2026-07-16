package com.example.healthtracker.ui.mealdiary.foodpicker

import com.example.healthtracker.domain.model.Food

data class FoodPickerUiState(
    val searchQuery: String = "",
    val foods: List<Food> = emptyList(),
)
