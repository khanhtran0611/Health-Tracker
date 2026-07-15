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


// Vì sao phải tạo abstract class:  Room dùng annotation processor (KSP/KAPT) để tự sinh ra một class con
// Và đồng thời triển khai code thực tế cho class con đó luôn, ta ko cần viết code này.
// Vì sao cần builder để build mà ko tạo thẳng object, lại phải qua abstract class + builder ?
// Là vì Room không biết bạn muốn cấu hình DB như thế nào —
// mà Builder cho phép bạn tùy biến rất nhiều thứ trước khi DB thực sự được tạo
// Ví dụ như: .addCallback(...) - seed data , .addMigrations(...) - Xử lý khi thay đổi schema


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
