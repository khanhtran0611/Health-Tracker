package com.example.healthtracker.core.reminder

import com.example.healthtracker.R

enum class ReminderType(
    val hour: Int,
    val minute: Int,
    val requestCode: Int,
    val messageRes: Int,
) {
    MORNING(hour = 12, minute = 35, requestCode = 1001, messageRes = R.string.reminder_message_morning),
    NOON(hour = 14, minute = 40, requestCode = 1002, messageRes = R.string.reminder_message_noon),
    EVENING(hour = 19, minute = 0, requestCode = 1003, messageRes = R.string.reminder_message_evening),
}
