package com.example.healthtracker.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.healthtracker.data.local.dao.ActivityDao
import com.example.healthtracker.data.local.dao.ActivityEntryDao
import com.example.healthtracker.data.local.dao.FoodDao
import com.example.healthtracker.data.local.dao.MealEntryDao
import com.example.healthtracker.data.local.dao.UserDao
import com.example.healthtracker.data.local.database.DatabaseSeed
import com.example.healthtracker.data.local.database.HealthTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        databaseProvider: Provider<HealthTrackerDatabase>,
    ): HealthTrackerDatabase {
        return Room.databaseBuilder(
            context,
            HealthTrackerDatabase::class.java,
            HealthTrackerDatabase.NAME,
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                    val database = databaseProvider.get()
                    val foodDao = database.foodDao()
                    val activityDao = database.activityDao()
                    DatabaseSeed.foods.forEach { foodDao.insert(it) }
                    DatabaseSeed.activities.forEach { activityDao.insert(it) }
                }
            }
        }).build()
    }

    @Provides
    fun provideUserDao(db: HealthTrackerDatabase): UserDao = db.userDao()

    @Provides
    fun provideFoodDao(db: HealthTrackerDatabase): FoodDao = db.foodDao()

    @Provides
    fun provideActivityDao(db: HealthTrackerDatabase): ActivityDao = db.activityDao()

    @Provides
    fun provideMealEntryDao(db: HealthTrackerDatabase): MealEntryDao = db.mealEntryDao()

    @Provides
    fun provideActivityEntryDao(db: HealthTrackerDatabase): ActivityEntryDao = db.activityEntryDao()
}
