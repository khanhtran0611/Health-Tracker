package com.example.healthtracker.core.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderScheduler: ReminderScheduler
    @Inject lateinit var reminderNotifier: ReminderNotifier

    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra(EXTRA_REMINDER_TYPE) ?: return

        if (name == ReminderScheduler.TEST_REMINDER_NAME) {
            reminderNotifier.showTestReminderNotification()
            return
        }

        val type = ReminderType.entries.find { it.name == name } ?: return
        reminderNotifier.showReminderNotification(type)
        reminderScheduler.schedule(type)
    }

    companion object {
        const val EXTRA_REMINDER_TYPE = "reminder_type"
    }
}
