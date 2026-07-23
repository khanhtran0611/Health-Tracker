package com.example.healthtracker.ui.mainshell

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.healthtracker.R
import com.example.healthtracker.ui.navigation.Route

/**
 * 5 tab của bottom nav — render bên trong [MainShellScreen]. Mỗi tab giữ 1
 * NavBackStack riêng (xem [TabBackStacks.kt]/[rememberTabBackStacks]).
 *
 * Nhãn dùng string resource (@StringRes) để hỗ trợ đổi ngôn ngữ. Icon
 * RestaurantMenu/DirectionsRun khớp đúng icon "eaten"/"burned" đã dùng ở
 * Dashboard (CalorieSummaryCard) — nhận diện nhất quán xuyên suốt app.
 */
enum class TopLevelTab(
    val route: Route,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    DASHBOARD(Route.Dashboard, R.string.tab_dashboard, Icons.Filled.Home),
    MEAL(Route.MealDiary, R.string.tab_meal_diary, Icons.Filled.RestaurantMenu),
    ACTIVITY(Route.ActivityDiary, R.string.tab_activity_diary, Icons.AutoMirrored.Filled.DirectionsRun),
    STATS(Route.Stats, R.string.tab_stats, Icons.Filled.Info),
    PROFILE(Route.Profile, R.string.tab_profile, Icons.Filled.Person),
    ;

    companion object {
        /** Tập route thuộc bottom nav. */
        val routes: Set<Route> = entries.map { it.route }.toSet()
    }
}
