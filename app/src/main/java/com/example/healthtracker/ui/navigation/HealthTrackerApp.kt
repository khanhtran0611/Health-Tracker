package com.example.healthtracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.healthtracker.ui.component.PlaceholderScreen

/**
 * Khung điều hướng của app: MỘT backstack chung cho mọi màn hình.
 * - Bottom bar chỉ hiện khi màn hiện tại thuộc [TopLevelTab] (ẩn ở AddEdit…, Settings…).
 * - Tab bottom nav gọi [navigateToTab]; màn con đi tiếp bằng backStack.add(...).
 *
 * TODO: start màn theo hasUser (Onboarding vs Dashboard) khi có UserRepository ViewModel.
 */
@Composable
fun HealthTrackerApp() {
    val backStack = rememberNavBackStack(Route.Onboarding)
    val currentRoute = backStack.lastOrNull() as? Route
    val showBottomBar = currentRoute != null && TopLevelTab.routes.contains(currentRoute)

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    TopLevelTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = { navigateToTab(backStack, tab.route) },
                            icon = { Icon(tab.icon, contentDescription = stringResource(tab.labelRes)) },
                            label = { Text(stringResource(tab.labelRes)) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.padding(padding),
            entryProvider = entryProvider {
                // ----- Onboarding (ngoài shell) -----
                entry<Route.Onboarding> {
                    PlaceholderScreen(
                        title = "Onboarding",
                        actions = listOf(
                            // Hoàn tất onboarding: xoá backstack để không back về được.
                            "Hoàn tất → vào app" to {
                                backStack.clear()
                                backStack.add(Route.Dashboard)
                            },
                        ),
                    )
                }

                // ----- 5 tab -----
                entry<Route.Dashboard> { PlaceholderScreen("Dashboard") }
                entry<Route.MealDiary> {
                    PlaceholderScreen(
                        "Meal Diary",
                        actions = listOf(
                            "＋ Thêm bữa ăn" to { backStack.add(Route.AddEditMealEntry()) },
                        ),
                    )
                }
                entry<Route.ActivityDiary> {
                    PlaceholderScreen(
                        "Activity Diary",
                        actions = listOf(
                            "＋ Thêm hoạt động" to { backStack.add(Route.AddEditActivityEntry()) },
                        ),
                    )
                }
                entry<Route.Stats> { PlaceholderScreen("Stats") }
                entry<Route.Profile> {
                    PlaceholderScreen(
                        "Profile",
                        actions = listOf(
                            "Chỉnh sửa hồ sơ" to { backStack.add(Route.EditProfile) },
                            "Cài đặt" to { backStack.add(Route.Settings) },
                        ),
                    )
                }

                // ----- Màn con Meal (ẩn bottom bar) -----
                entry<Route.AddEditMealEntry> { route ->
                    PlaceholderScreen(
                        if (route.entryId == null) "Thêm bữa ăn" else "Sửa bữa ăn #${route.entryId}",
                        actions = listOf(
                            "Chọn món (Food Picker)" to { backStack.add(Route.FoodPicker) },
                        ),
                    )
                }
                entry<Route.FoodPicker> {
                    PlaceholderScreen(
                        "Food Picker",
                        actions = listOf(
                            "Nhập món thủ công" to { backStack.add(Route.EnterFoodManually) },
                        ),
                    )
                }
                entry<Route.EnterFoodManually> { PlaceholderScreen("Enter Food Manually") }

                // ----- Màn con Activity (ẩn bottom bar) -----
                entry<Route.AddEditActivityEntry> { route ->
                    PlaceholderScreen(
                        if (route.entryId == null) "Thêm hoạt động" else "Sửa hoạt động #${route.entryId}",
                        actions = listOf(
                            "Chọn hoạt động" to { backStack.add(Route.ChooseActivity) },
                        ),
                    )
                }
                entry<Route.ChooseActivity> {
                    PlaceholderScreen(
                        "Choose Activity",
                        actions = listOf(
                            "Nhập hoạt động thủ công" to { backStack.add(Route.EnterActivityManually) },
                        ),
                    )
                }
                entry<Route.EnterActivityManually> { PlaceholderScreen("Enter Activity Manually") }

                // ----- Màn con Profile (ẩn bottom bar) -----
                entry<Route.Settings> { PlaceholderScreen("Settings") }
                entry<Route.EditProfile> { PlaceholderScreen("Edit Profile") }
            },
        )
    }
}
