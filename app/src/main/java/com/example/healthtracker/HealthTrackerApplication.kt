package com.example.healthtracker

import android.app.Application
import com.example.healthtracker.core.reminder.ReminderNotifier
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HealthTrackerApplication : Application() {

    @Inject lateinit var reminderNotifier: ReminderNotifier

    override fun onCreate() {
        super.onCreate()
        reminderNotifier.createNotificationChannel()
    }
}
