package com.example.healthtracker.core.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Alarm nổ tới đây: hiện notification cho đúng [ReminderType], rồi tự đặt lại
 * lịch cho CÙNG khung giờ này vào ngày mai — alarm ở đây là one-shot (không
 * phải repeating) nên phải tự gia hạn mỗi lần nổ.
 *
 * KHÔNG cần đọc lại DataStore ở đây: "tắt reminder" đã huỷ PendingIntent tương
 * ứng ngay lúc toggle off (xem SettingsViewModel), nên hễ receiver này chạy
 * nghĩa là reminder đó chắc chắn đang bật — không có nguy cơ hiện nhầm khi đã tắt.
 */
@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderScheduler: ReminderScheduler
    @Inject lateinit var reminderNotifier: ReminderNotifier

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_REMINDER_TYPE)
            ?.let { name -> ReminderType.entries.find { it.name == name } }
            ?: return

        reminderNotifier.showReminderNotification(type)
        reminderScheduler.schedule(type)
    }

    companion object {
        const val EXTRA_REMINDER_TYPE = "reminder_type"
    }
}
