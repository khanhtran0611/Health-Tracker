package com.example.healthtracker

import android.app.Application
import com.example.healthtracker.core.reminder.ReminderNotifier
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/** Entry point của Hilt cho toàn app. */
@HiltAndroidApp
class HealthTrackerApplication : Application() {

    @Inject lateinit var reminderNotifier: ReminderNotifier

    override fun onCreate() {
        super.onCreate()
        reminderNotifier.createNotificationChannel()
    }
}
