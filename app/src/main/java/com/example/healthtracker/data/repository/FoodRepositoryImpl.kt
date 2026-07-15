package com.example.healthtracker.data.repository

import com.example.healthtracker.data.local.dao.FoodDao
import com.example.healthtracker.data.mapper.toDomain
import com.example.healthtracker.data.mapper.toEntity
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
) : FoodRepository {

    override fun observeFoods(): Flow<List<Food>> =
        foodDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun searchFoods(query: String): Flow<List<Food>> =
        foodDao.search(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getFood(id: Long): Food? = foodDao.getById(id)?.toDomain()

    override suspend fun addFood(food: Food): Long = foodDao.insert(food.toEntity())

    override suspend fun updateFood(food: Food) = foodDao.update(food.toEntity())

    override suspend fun deleteFood(food: Food) = foodDao.delete(food.toEntity())
}
