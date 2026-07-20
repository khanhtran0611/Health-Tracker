package com.example.healthtracker.data.repository

import com.example.healthtracker.data.local.dao.MealEntryDao
import com.example.healthtracker.data.mapper.toDomain
import com.example.healthtracker.data.mapper.toEntity
import com.example.healthtracker.domain.model.MealEntry
import com.example.healthtracker.domain.repository.MealEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class MealEntryRepositoryImpl @Inject constructor(
    private val mealEntryDao: MealEntryDao,
) : MealEntryRepository {

    override fun observeEntriesByDate(date: LocalDate): Flow<List<MealEntry>> =
        mealEntryDao.observeByDate(date).map { list -> list.map { it.toDomain() } }

    override fun observeTotalCaloriesByDate(date: LocalDate): Flow<Double> =
        mealEntryDao.observeTotalCaloriesByDate(date)

    override suspend fun getEntry(id: Long): MealEntry? =
        mealEntryDao.getById(id)?.toDomain()

    override suspend fun addEntry(entry: MealEntry): Long =
        mealEntryDao.insert(entry.toEntity())

    override suspend fun updateEntry(entry: MealEntry) {
        val existing = mealEntryDao.getById(entry.id) ?: return
        // Giữ nguyên thời điểm log gốc.
        mealEntryDao.update(entry.toEntity().copy(createdAt = existing.createdAt))
    }

    override suspend fun deleteEntry(entry: MealEntry) =
        mealEntryDao.delete(entry.toEntity())

    override suspend fun deleteAllEntries() = mealEntryDao.deleteAll()
}
