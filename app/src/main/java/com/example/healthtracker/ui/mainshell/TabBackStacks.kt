package com.example.healthtracker.ui.mainshell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.healthtracker.ui.navigation.Route

/**
 * Mỗi tab bottom-nav giữ 1 [NavBackStack] RIÊNG (không dùng chung 1 backstack
 * cho cả 5 tab) — nhờ vậy rời tab A sang tab B rồi quay lại A, backstack và
 * mọi state/scroll của A vẫn còn nguyên, không bị mất/reset (đúng mục tiêu ghi
 * trong CLAUDE.md). [MainShellScreen] chỉ đổi backstack nào đang được NavDisplay
 * tầng trong hiển thị, KHÔNG xoá/tạo lại backstack của tab không active.
 */
@Composable
fun rememberTabBackStacks(): Map<Route, NavBackStack<NavKey>> {
    val dashboard = rememberNavBackStack(Route.Dashboard)
    val mealDiary = rememberNavBackStack(Route.MealDiary)
    val activityDiary = rememberNavBackStack(Route.ActivityDiary)
    val stats = rememberNavBackStack(Route.Stats)
    val profile = rememberNavBackStack(Route.Profile)

    return remember(dashboard, mealDiary, activityDiary, stats, profile) {
        mapOf(
            Route.Dashboard to dashboard,
            Route.MealDiary to mealDiary,
            Route.ActivityDiary to activityDiary,
            Route.Stats to stats,
            Route.Profile to profile,
        )
    }
}
