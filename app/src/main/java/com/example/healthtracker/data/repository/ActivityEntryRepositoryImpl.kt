package com.example.healthtracker.data.repository

import com.example.healthtracker.data.local.dao.ActivityEntryDao
import com.example.healthtracker.data.mapper.toDomain
import com.example.healthtracker.data.mapper.toEntity
import com.example.healthtracker.domain.model.ActivityEntry
import com.example.healthtracker.domain.repository.ActivityEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ActivityEntryRepositoryImpl @Inject constructor(
    private val activityEntryDao: ActivityEntryDao,
) : ActivityEntryRepository {

    override fun observeEntriesByDate(date: LocalDate): Flow<List<ActivityEntry>> =
        activityEntryDao.observeByDate(date).map { list -> list.map { it.toDomain() } }

    override fun observeTotalCaloriesBurnedByDate(date: LocalDate): Flow<Double> =
        activityEntryDao.observeTotalCaloriesBurnedByDate(date)

    override fun observeDailyTotalsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<Map<LocalDate, Double>> =
        activityEntryDao.observeDailyTotalsBetween(startDate, endDate).map { list -> list.associate { it.logDate to it.total } }

    override suspend fun getEntry(id: Long): ActivityEntry? =
        activityEntryDao.getById(id)?.toDomain()

    override suspend fun addEntry(entry: ActivityEntry): Long =
        activityEntryDao.insert(entry.toEntity())

    override suspend fun updateEntry(entry: ActivityEntry) {
        val existing = activityEntryDao.getById(entry.id) ?: return
        // Giữ nguyên thời điểm log gốc.
        activityEntryDao.update(entry.toEntity().copy(createdAt = existing.createdAt))
    }

    override suspend fun deleteEntry(entry: ActivityEntry) =
        activityEntryDao.delete(entry.toEntity())

    override suspend fun deleteAllEntries() = activityEntryDao.deleteAll()
}
