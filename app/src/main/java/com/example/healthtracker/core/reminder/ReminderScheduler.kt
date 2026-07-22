package com.example.healthtracker.core.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.getSystemService
import com.example.healthtracker.domain.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bọc AlarmManager cho 3 khung giờ nhắc ghi nhật ký. Dùng `setAndAllowWhileIdle`
 * (KHÔNG dùng setExactAndAllowWhileIdle) — trễ vài phút do Doze/battery
 * optimization là chấp nhận được cho 1 lời nhắc, đổi lại KHÔNG cần xin quyền
 * SCHEDULE_EXACT_ALARM (chỉ bắt buộc cho alarm chính xác từ Android 12/API 31).
 */
@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val alarmManager = context.getSystemService<AlarmManager>()

    fun canScheduleExactAlarms(context: Context): Boolean {
        val alarmManager = context.getSystemService<AlarmManager>() ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 (API 31) trở lên -> phải kiểm tra quyền thật sự
            alarmManager.canScheduleExactAlarms()
        } else {
            // Android 26-30 -> không cần xin quyền gì, mặc định luôn được phép
            true
        }
    }

    fun requestExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        }
        // Android < 31: không cần làm gì, vì mặc định đã được phép rồi
    }

    fun schedule(type: ReminderType) {
        val triggerAtMillis = nextTriggerTimeMillis(type.hour, type.minute)
        val alarmManager = context.getSystemService<AlarmManager>()
        val canSchedule = canScheduleExactAlarms(context)


        if (!canSchedule) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        }

        // RTC_WAKEUP: quyết định loại đồng hồ dùng để tính thời gian,
        // và có đánh thức màn hình/CPU dậy hay không
        alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntentFor(type))
    }

    fun cancel(type: ReminderType) {
        // Hàm này gỡ bỏ báo thức tương ứng với pendingIntent đó ra khỏi danh sách hẹn giờ đang chờ của AlarmManager
        alarmManager?.cancel(pendingIntentFor(type))
    }

    /** Đồng bộ cả 3 loại theo đúng trạng thái đang lưu trong [settings] — bật thì schedule, tắt thì cancel. */
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
        // gửi 1 broadcast (tức gọi tới BroadcastReceiver)
        // khi tới lúc thực thi — thay vì mở Activity hay khởi động Service
        return PendingIntent.getBroadcast(
            context,
            type.requestCode,
            intent,
            // nếu đã tồn tại sẵn 1 PendingIntent với cùng requestCode + cùng Intent component,
            // thì cập nhật dữ liệu (extras) bên trong Intent mới nhất vào PendingIntent cũ đó, thay vì tạo bản sao khác

            // FLAG_IMMUTABLE — đánh dấu PendingIntent này là bất biến, nghĩa là bên nhận nó
            // (ở đây là AlarmManager, thuộc tiến trình hệ thống)
            // không được phép chỉnh sửa nội dung Intent bên trong trước khi gửi đi
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    /** Mốc thời gian kế tiếp có đúng [hour]:[minute] — hôm nay nếu chưa qua giờ đó, mai nếu đã qua rồi. */
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
