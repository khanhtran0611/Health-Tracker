package com.example.healthtracker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.healthtracker.domain.model.MealType
import com.example.healthtracker.ui.component.PlaceholderScreen
import com.example.healthtracker.ui.mealdiary.MealDiaryScreen
import com.example.healthtracker.ui.mealdiary.foodpicker.FoodPickerScreen
import com.example.healthtracker.ui.onboarding.OnboardingScreen
import com.example.healthtracker.ui.profile.EditProfileScreen

/**
 * Gốc app: chờ [AppStartViewModel] xác định đã có hồ sơ user chưa rồi mới dựng
 * backstack — tránh chớp màn Onboarding rồi nhảy sang Dashboard.
 */
@Composable
fun HealthTrackerApp() {
    val appStartViewModel: AppStartViewModel = hiltViewModel()
    val startDestination by appStartViewModel.startDestination.collectAsStateWithLifecycle()

    when (startDestination) {
        AppStartDestination.LOADING -> LoadingScreen()
        AppStartDestination.ONBOARDING -> HealthTrackerNavHost(startRoute = Route.Onboarding)
        AppStartDestination.DASHBOARD -> HealthTrackerNavHost(startRoute = Route.Dashboard)
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

/**
 * Khung điều hướng của app: MỘT backstack chung cho mọi màn hình.
 * - Bottom bar chỉ hiện khi màn hiện tại thuộc [TopLevelTab] (ẩn ở AddEdit…, Settings…).
 * - Tab bottom nav gọi [navigateToTab]; màn con đi tiếp bằng backStack.add(...).
 */
@Composable
private fun HealthTrackerNavHost(startRoute: Route) {
    val backStack = rememberNavBackStack(startRoute)
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
                    OnboardingScreen(
                        onFinishOnboarding = {
                            // Xoá backstack để không back về Onboarding được nữa.
                            backStack.clear()
                            backStack.add(Route.Dashboard)
                        },
                    )
                }

                // ----- 5 tab -----
                entry<Route.Dashboard> { PlaceholderScreen("Dashboard") }
                entry<Route.MealDiary> {
                    MealDiaryScreen(
                        onAddFood = { mealType -> backStack.add(Route.FoodPicker(mealType = mealType)) },
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
                            // Màn này còn placeholder nên chưa có mealType thật — tạm BREAKFAST
                            // cho demo; khi làm AddEditMealEntry thật sẽ tự mang đúng mealType.
                            "Chọn món (Food Picker)" to { backStack.add(Route.FoodPicker(mealType = MealType.BREAKFAST)) },
                        ),
                    )
                }
                entry<Route.FoodPicker> {
                    FoodPickerScreen(
                        onBack = { backStack.removeLastOrNull() },
                        onFoodSelected = { /* TODO: điều hướng sang Add/Edit Meal Entry khi làm tới, mang theo food.id */ },
                        onEnterNewFood = { backStack.add(Route.EnterFoodManually) },
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
                entry<Route.EditProfile> {
                    EditProfileScreen(onBack = { backStack.removeLastOrNull() })
                }
            },
        )
    }
}
