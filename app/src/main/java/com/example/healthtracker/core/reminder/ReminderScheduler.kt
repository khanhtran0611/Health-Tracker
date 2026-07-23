package com.example.healthtracker.core.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import com.example.healthtracker.domain.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val alarmManager = context.getSystemService<AlarmManager>()

    fun schedule(type: ReminderType) {
        val triggerAtMillis = nextTriggerTimeMillis(type.hour, type.minute)

        alarmManager?.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntentFor(type))
    }

    fun cancel(type: ReminderType) {

        alarmManager?.cancel(pendingIntentFor(type))
    }

    fun rescheduleAll(settings: AppSettings) {
        ReminderType.entries.forEach { type ->
            if (settings.remindersEnabled && isTypeEnabled(type, settings)) {
                schedule(type)
            } else {
                cancel(type)
            }
        }
    }

    private fun isTypeEnabled(type: ReminderType, settings: AppSettings): Boolean = when (type) {
        ReminderType.MORNING -> settings.morningReminderEnabled
        ReminderType.NOON -> settings.noonReminderEnabled
        ReminderType.EVENING -> settings.eveningReminderEnabled
    }

    private fun pendingIntentFor(type: ReminderType): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_REMINDER_TYPE, type.name)
        }

        return PendingIntent.getBroadcast(
            context,
            type.requestCode,
            intent,

            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun nextTriggerTimeMillis(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return calendar.timeInMillis
    }
}
