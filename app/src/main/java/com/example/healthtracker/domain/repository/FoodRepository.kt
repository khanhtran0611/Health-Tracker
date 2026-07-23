package com.example.healthtracker.domain.repository

import com.example.healthtracker.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun observeFoods(): Flow<List<Food>>
    fun searchFoods(query: String): Flow<List<Food>>
    suspend fun getFood(id: Long): Food?

    suspend fun addFood(food: Food): Long
    suspend fun updateFood(food: Food)

    suspend fun deleteFood(food: Food)
}
