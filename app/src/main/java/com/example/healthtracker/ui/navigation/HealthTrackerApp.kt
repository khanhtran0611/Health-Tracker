package com.example.healthtracker.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.healthtracker.ui.component.PlaceholderScreen
import com.example.healthtracker.ui.mealdiary.MealDiaryScreen
import com.example.healthtracker.ui.mealdiary.enterfood.EnterFoodManuallyScreen
import com.example.healthtracker.ui.mealdiary.foodpicker.FoodPickerScreen
import com.example.healthtracker.ui.onboarding.OnboardingScreen
import com.example.healthtracker.ui.profile.EditProfileScreen
import com.example.healthtracker.ui.toast.SharedToast
import com.example.healthtracker.ui.toast.ToastData
import com.example.healthtracker.ui.toast.ToastViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

// Thời lượng animation chuyển màn (slide + fade) khi push/pop trong NavDisplay.
private const val NAV_TRANSITION_DURATION_MS = 300

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

    // Toast dùng chung toàn app: bất kỳ ViewModel nào cũng gửi message được
    // (qua ToastController), hiện ở ĐÚNG 1 chỗ này vì đây là nơi duy nhất sống
    // xuyên suốt mọi lần chuyển màn — xem thêm ui/toast/ToastController.kt.
    val toastViewModel: ToastViewModel = hiltViewModel()
    // vì toast có thể null => phải type "T?", không phải mỗi "T"
    var currentToast by remember { mutableStateOf<ToastData?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        toastViewModel.messages.collect { message ->
            // Chờ pop/push transition của NavDisplay chạy xong rồi mới hiện toast,
            // để enter animation không bị rớt frame vì main thread đang bận vẽ transition.
            delay(NAV_TRANSITION_DURATION_MS.toLong())

            currentToast = ToastData(message = stringResource(message.textRes), type = message.type)
            delay(3000)
            currentToast = null
        }
    }

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
        Box(modifier = Modifier.fillMaxSize()) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                modifier = Modifier.fillMaxSize().padding(padding),
                // Đi tới màn mới: màn mới trượt vào từ bên phải + fade in,
                // màn cũ trượt sang trái + fade out.
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(NAV_TRANSITION_DURATION_MS),
                    ) + fadeIn(animationSpec = tween(NAV_TRANSITION_DURATION_MS)) togetherWith
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(NAV_TRANSITION_DURATION_MS),
                        ) + fadeOut(animationSpec = tween(NAV_TRANSITION_DURATION_MS))
                },
                // Back (pop): ngược lại — màn trước trượt vào từ bên trái + fade in,
                // màn hiện tại trượt sang phải + fade out.
                popTransitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(NAV_TRANSITION_DURATION_MS),
                    ) + fadeIn(animationSpec = tween(NAV_TRANSITION_DURATION_MS)) togetherWith
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(NAV_TRANSITION_DURATION_MS),
                        ) + fadeOut(animationSpec = tween(NAV_TRANSITION_DURATION_MS))
                },
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
                        onAddFood = { mealType, logDate ->
                            backStack.add(Route.FoodPicker(mealType = mealType, logDate = logDate.toString()))
                        },
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
                entry<Route.FoodPicker> { route ->
                    FoodPickerScreen(
                        mealType = route.mealType,
                        logDate = LocalDate.parse(route.logDate),
                        onBack = { backStack.removeLastOrNull() },
                        onEnterNewFood = { backStack.add(Route.EnterFoodManually()) },
                        onEditFood = { food -> backStack.add(Route.EnterFoodManually(food = food)) },
                    )
                }
                entry<Route.EnterFoodManually> { route ->
                    EnterFoodManuallyScreen(
                        food = route.food,
                        onClose = { backStack.removeLastOrNull() },
                    )
                }

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
            val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 64.dp + 8.dp
            SharedToast(
                toastData = currentToast,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = topPadding)
            )
        }
    }
}
