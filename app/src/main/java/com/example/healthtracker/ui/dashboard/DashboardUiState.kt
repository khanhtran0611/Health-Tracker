package com.example.healthtracker.ui.dashboard

import java.time.LocalDate

/** Trạng thái calo hôm nay so với TDEE — quyết định lời khuyên + nút gợi ý trên Dashboard. */
enum class CalorieStatus {
    /** Còn dư calo (remaining > 0) — gợi ý thêm bữa ăn. */
    UNDER_TARGET,
    /** Vừa đủ calo (remaining == 0) — không gợi ý gì thêm. */
    ON_TARGET,
    /** Đã vượt calo (remaining < 0) — gợi ý thêm hoạt động để đốt bớt. */
    OVER_TARGET,
}

data class DashboardUiState(
    val today: LocalDate = LocalDate.now(),
    /** TDEE đã điều chỉnh theo goal — 0.0 khi chưa có hồ sơ user. */
    val tdee: Double = 0.0,
    val eatenToday: Double = 0.0,
    val burnedToday: Double = 0.0,
) {
    /** Remaining = TDEE − Eaten + Burned — KHÔNG phải TDEE − Eaten suông. */
    val remaining: Double
        get() = tdee - eatenToday + burnedToday

    /** Balance chỉ để hiển thị, KHÔNG dùng để ra lời khuyên. */
    val balance: Double
        get() = eatenToday - burnedToday

    /**
     * Tỉ lệ đã ăn / ngân sách thật (TDEE + burned) — vẽ CalorieProgressCircle.
     * Chia cho (tdee + burnedToday), KHÔNG phải riêng tdee, để khớp đúng công
     * thức remaining ở trên: progress > 1f <=> remaining < 0 (đã vượt quá thật
     * sự, có tính cả calo đã đốt) — CalorieProgressCircle dùng chính ngưỡng
     * này để đổi màu đỏ, và tự coerce về [0,1] khi vẽ cung.
     */
    val progress: Float
        get() {
            val effectiveBudget = tdee + burnedToday
            return if (effectiveBudget <= 0.0) 0f else (eatenToday / effectiveBudget).toFloat()
        }

    /**
     * Dựa trên remaining ĐÃ LÀM TRÒN (Int) — khớp đúng con số hiển thị cho
     * user, tránh trường hợp remaining = -0.2 hiển thị "0" nhưng status vẫn
     * báo OVER_TARGET (lệch giữa số và trạng thái).
     */
    val calorieStatus: CalorieStatus
        get() = when {
            remaining.toInt() > 0 -> CalorieStatus.UNDER_TARGET
            remaining.toInt() < 0 -> CalorieStatus.OVER_TARGET
            else -> CalorieStatus.ON_TARGET
        }
}
