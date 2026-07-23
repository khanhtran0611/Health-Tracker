package com.example.healthtracker.ui.mainshell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.healthtracker.ui.navigation.Route

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
