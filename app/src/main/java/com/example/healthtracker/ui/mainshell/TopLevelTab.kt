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

        val routes: Set<Route> = entries.map { it.route }.toSet()
    }
}
