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

data class StatisticsUiState(
    /** 7 ngày gần nhất, sắp xếp cũ → mới (ngày cuối cùng là hôm nay). */
    val dailyStats: List<DailyCalorieStat> = emptyList(),
    /** TDEE hiện tại của user — dùng để xét "đạt mục tiêu" cho từng ngày. 0.0 khi chưa có hồ sơ. */
    val tdee: Double = 0.0,
    val isLoading: Boolean = true,
) {
    val avgEatenPerDay: Double
        get() = if (dailyStats.isEmpty()) 0.0 else dailyStats.sumOf { it.eaten } / dailyStats.size

    val avgBurnedPerDay: Double
        get() = if (dailyStats.isEmpty()) 0.0 else dailyStats.sumOf { it.burned } / dailyStats.size

    val daysGoalMet: Int
        get() = dailyStats.count { it.isGoalMet(tdee) }
}
