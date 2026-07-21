package com.example.healthtracker.core.reminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.healthtracker.MainActivity
import com.example.healthtracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Tạo NotificationChannel (1 lần lúc app khởi động) + build/hiện notification cho từng [ReminderType]. */
@Singleton
class ReminderNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.reminder_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = context.getString(R.string.reminder_channel_description)
        }
        // lấy NotificationManager (system service,
        // giống cách lấy AlarmManager đã nói ở câu trả lời trước)
        // rồi đăng ký NotificationChannel với hệ thống.
        context.getSystemService<NotificationManager>()?.createNotificationChannel(channel)
    }

    fun showReminderNotification(type: ReminderType) {
        // Từ Android 13 (API 33) phải xin quyền POST_NOTIFICATIONS mới hiện được
        // notification — chưa có quyền thì bỏ qua thay vì để hệ thống crash/no-op
        // âm thầm. Trên API < 33, hàm check này luôn trả GRANTED (quyền không áp
        // dụng), nên không cần rẽ nhánh theo Build.VERSION.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            type.requestCode,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )


        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.reminder_notification_title))
            .setContentText(context.getString(type.messageRes))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        NotificationManagerCompat.from(context).notify(type.requestCode, notification)
    }

    companion object {
        const val CHANNEL_ID = "meal_reminders"
    }
}
