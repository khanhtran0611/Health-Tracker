package com.example.healthtracker.core.reminder

import com.example.healthtracker.R

/**
 * 3 khung giờ nhắc ghi nhật ký cố định theo đề (7h sáng / 12h trưa / 19h tối).
 * [requestCode] dùng làm request code khi tạo `PendingIntent.getBroadcast(...)` —
 * phải khác nhau giữa 3 loại để không đè PendingIntent của nhau.
 */
enum class ReminderType(
    val hour: Int,
    val minute: Int,
    val requestCode: Int,
    val messageRes: Int,
) {
    MORNING(hour = 7, minute = 0, requestCode = 1001, messageRes = R.string.reminder_message_morning),
    NOON(hour = 10, minute = 25, requestCode = 1002, messageRes = R.string.reminder_message_noon),
    EVENING(hour = 19, minute = 0, requestCode = 1003, messageRes = R.string.reminder_message_evening),
}
