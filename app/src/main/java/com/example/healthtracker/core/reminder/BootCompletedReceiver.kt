package com.example.healthtracker.core.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.healthtracker.domain.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * AlarmManager bị hệ thống xoá sạch mỗi khi khởi động lại máy — receiver này
 * chạy đúng 1 lần lúc boot xong, đọc lại AppSettings từ DataStore rồi đặt lại
 * lịch cho các reminder đang bật.
 *
 * onReceive() không phải hàm suspend nhưng cần đọc DataStore (suspend) -> dùng
 * goAsync() xin hệ thống cho thêm thời gian chạy nền, chạy trong 1 CoroutineScope
 * riêng rồi gọi pendingResult.finish() khi xong (bắt buộc, không thì hệ thống có
 * thể giết tiến trình trước khi coroutine kịp hoàn tất).
 */
@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject lateinit var settingsRepository: SettingsRepository
    @Inject lateinit var reminderScheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val settings = settingsRepository.observeSettings().first()
                reminderScheduler.rescheduleAll(settings)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
