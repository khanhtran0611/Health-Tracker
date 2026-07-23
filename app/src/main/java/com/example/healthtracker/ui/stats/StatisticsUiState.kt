package com.example.healthtracker.ui.stats

import java.time.LocalDate

/** Calo ăn/đốt của 1 ngày — dùng để vẽ bar/line chart và tính thống kê tuần. */
data class DailyCalorieStat(
    val date: LocalDate,
    val eaten: Double,
    val burned: Double,
    val toleranceKcal: Double = 50.0
) {
    /** Đạt mục tiêu = không vượt quá TDEE ngày đó, cùng công thức remaining với Dashboard. */
    fun isGoalMet(tdee: Double): Boolean = kotlin.math.abs(tdee - eaten + burned) <= toleranceKcal
}

/**
 * Số liệu tổng kết 30 ngày gần nhất — LUÔN cố định theo hôm nay, không đổi khi
 * user lùi/tiến xem cửa sổ 7 ngày khác trên [StatsDateRangeNavigator] (khác
 * [StatisticsUiState.dailyStats], vốn đổi theo cửa sổ đang xem).
 */
data class MonthlySummary(
    val avgEatenPerDay: Double = 0.0,
    val avgBurnedPerDay: Double = 0.0,
    val daysGoalMet: Int = 0,
    val totalDays: Int = 30,
)

data class StatisticsUiState(
    /** 7 ngày trong cửa sổ đang xem, sắp xếp cũ → mới (KHÔNG chắc kết thúc ở hôm nay — có thể đã lùi về quá khứ). */
    val dailyStats: List<DailyCalorieStat> = emptyList(),
    /** TDEE hiện tại của user — dùng để xét "đạt mục tiêu" cho từng ngày. 0.0 khi chưa có hồ sơ. */
    val tdee: Double = 0.0,
    /** Ngày đầu/cuối của cửa sổ 7 ngày đang xem — hiển thị trên [com.example.healthtracker.ui.stats.components.StatsDateRangeNavigator]. */
    val startDate: LocalDate = LocalDate.now().minusDays(6),
    val endDate: LocalDate = LocalDate.now(),
    /** Còn lùi thêm 7 ngày nữa được không — false khi đã chạm giới hạn 30 ngày lịch sử. */
    val canGoToPreviousRange: Boolean = true,
    /** Còn tiến thêm 7 ngày được không — false khi đang ở đúng 7 ngày gần nhất (không cho xem tương lai). */
    val canGoToNextRange: Boolean = false,
    val monthlySummary: MonthlySummary = MonthlySummary(),
) {
    val avgEatenPerDay: Double
        get() = if (dailyStats.isEmpty()) 0.0 else dailyStats.sumOf { it.eaten } / dailyStats.size

    val avgBurnedPerDay: Double
        get() = if (dailyStats.isEmpty()) 0.0 else dailyStats.sumOf { it.burned } / dailyStats.size

    val daysGoalMet: Int
        get() = dailyStats.count { it.isGoalMet(tdee) }
}
