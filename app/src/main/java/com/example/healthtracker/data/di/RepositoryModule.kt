package com.example.healthtracker.data.di

import com.example.healthtracker.data.repository.ActivityEntryRepositoryImpl
import com.example.healthtracker.data.repository.ActivityRepositoryImpl
import com.example.healthtracker.data.repository.FoodRepositoryImpl
import com.example.healthtracker.data.repository.MealEntryRepositoryImpl
import com.example.healthtracker.data.repository.SettingsRepositoryImpl
import com.example.healthtracker.data.repository.UserRepositoryImpl
import com.example.healthtracker.domain.repository.ActivityEntryRepository
import com.example.healthtracker.domain.repository.ActivityRepository
import com.example.healthtracker.domain.repository.FoodRepository
import com.example.healthtracker.domain.repository.MealEntryRepository
import com.example.healthtracker.domain.repository.SettingsRepository
import com.example.healthtracker.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindFoodRepository(impl: FoodRepositoryImpl): FoodRepository

    @Binds
    @Singleton
    abstract fun bindActivityRepository(impl: ActivityRepositoryImpl): ActivityRepository

    @Binds
    @Singleton
    abstract fun bindMealEntryRepository(impl: MealEntryRepositoryImpl): MealEntryRepository

    @Binds
    @Singleton
    abstract fun bindActivityEntryRepository(impl: ActivityEntryRepositoryImpl): ActivityEntryRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
