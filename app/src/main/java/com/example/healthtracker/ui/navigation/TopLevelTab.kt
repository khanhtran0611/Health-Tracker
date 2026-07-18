package com.example.healthtracker.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.healthtracker.R

/**
 * 5 tab của bottom nav. Chỉ những Route nằm ở đây mới hiện bottom bar; các màn khác
 * (AddEdit…, FoodPicker, Settings…) sẽ ẩn bottom bar.
 *
 * Nhãn dùng string resource (@StringRes) để hỗ trợ đổi ngôn ngữ.
 * Icon tạm dùng bộ material core (khỏi thêm material-icons-extended) — chỉnh sau khi ráp UI thật.
 */
enum class TopLevelTab(
    val route: Route,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    DASHBOARD(Route.Dashboard, R.string.tab_dashboard, Icons.Filled.Home),
    MEAL(Route.MealDiary, R.string.tab_meal_diary, Icons.AutoMirrored.Filled.List),
    ACTIVITY(Route.ActivityDiary, R.string.tab_activity_diary, Icons.Filled.PlayArrow),
    STATS(Route.Stats, R.string.tab_stats, Icons.Filled.Info),
    PROFILE(Route.Profile, R.string.tab_profile, Icons.Filled.Person),
    ;

    companion object {
        /** Tập route thuộc bottom nav — dùng để quyết định ẩn/hiện bottom bar. */
        val routes: Set<Route> = entries.map { it.route }.toSet()
    }
}
