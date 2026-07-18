package com.example.healthtracker.ui.activitydiary.addactivityentry

import com.example.healthtracker.domain.model.Activity
import java.time.LocalDate

data class AddActivityEntryUiState(
    /** null = đang load activity/user từ DB. */
    val activity: Activity? = null,
    val durationMinutes: Int = 30,
    val logDate: LocalDate = LocalDate.now(),
    /** Cân nặng user tại thời điểm mở màn — lấy từ UserRepository, KHÔNG lưu vào state lâu dài. */
    val weightKg: Double = 0.0,
    val isSaving: Boolean = false,
) {
    /** Calo hoạt động = MET × weight(kg) × duration(giờ) — luôn tính lại, không lưu sẵn. */
    val caloriesBurned: Double
        get() = (activity?.met ?: 0.0) * weightKg * (durationMinutes / 60.0)
}
