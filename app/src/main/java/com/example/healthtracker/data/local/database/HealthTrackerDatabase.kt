package com.example.healthtracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.healthtracker.data.local.dao.ActivityDao
import com.example.healthtracker.data.local.dao.ActivityEntryDao
import com.example.healthtracker.data.local.dao.FoodDao
import com.example.healthtracker.data.local.dao.MealEntryDao
import com.example.healthtracker.data.local.dao.UserDao
import com.example.healthtracker.data.local.entity.ActivityEntity
import com.example.healthtracker.data.local.entity.ActivityEntryEntity
import com.example.healthtracker.data.local.entity.FoodEntity
import com.example.healthtracker.data.local.entity.MealEntryEntity
import com.example.healthtracker.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        FoodEntity::class,
        ActivityEntity::class,
        MealEntryEntity::class,
        ActivityEntryEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class HealthTrackerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun foodDao(): FoodDao
    abstract fun activityDao(): ActivityDao
    abstract fun mealEntryDao(): MealEntryDao
    abstract fun activityEntryDao(): ActivityEntryDao

    companion object {
        const val NAME = "health_tracker.db"
    }
}
