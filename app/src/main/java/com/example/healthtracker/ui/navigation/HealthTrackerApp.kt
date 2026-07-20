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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.healthtracker.ui.activitydiary.activitypicker.ActivityPickerScreen
import com.example.healthtracker.ui.activitydiary.enteractivity.EnterActivityScreen
import com.example.healthtracker.ui.mainshell.MainShellScreen
import com.example.healthtracker.ui.mealdiary.enterfood.EnterFoodManuallyScreen
import com.example.healthtracker.ui.mealdiary.foodpicker.FoodPickerScreen
import com.example.healthtracker.ui.onboarding.OnboardingScreen
import com.example.healthtracker.ui.profile.EditProfileScreen
import com.example.healthtracker.ui.settings.SettingScreen
import com.example.healthtracker.ui.toast.SharedToast
import com.example.healthtracker.ui.toast.ToastData
import com.example.healthtracker.ui.toast.ToastViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

// Thời lượng animation chuyển màn (slide + fade) khi push/pop trong NavDisplay tầng ngoài.
private const val NAV_TRANSITION_DURATION_MS = 300

/**
 * Gốc app: chờ [AppStartViewModel] xác định đã có hồ sơ user chưa rồi mới dựng
 * backstack — tránh chớp màn Onboarding rồi nhảy sang MainShell.
 */
@Composable
fun HealthTrackerApp() {
    val appStartViewModel: AppStartViewModel = hiltViewModel()
    val startDestination by appStartViewModel.startDestination.collectAsStateWithLifecycle()

    when (startDestination) {
        AppStartDestination.LOADING -> LoadingScreen()
        AppStartDestination.ONBOARDING -> HealthTrackerNavHost(startRoute = Route.Onboarding)
        AppStartDestination.MAIN_SHELL -> HealthTrackerNavHost(startRoute = Route.MainShell)
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

/**
 * Khung điều hướng TẦNG NGOÀI — 2 tầng NavDisplay lồng nhau:
 * - Tầng ngoài (ở đây): Onboarding, [Route.MainShell] (đại diện cho cả shell
 *   5-tab), và mọi màn con đứng NGOÀI phạm vi 1 tab cụ thể (FoodPicker,
 *   EnterFoodManually, ChooseActivity, EnterActivityManually, Settings,
 *   EditProfile) — những màn này ẩn bottom bar vì bottom bar giờ sống hẳn bên
 *   trong [MainShellScreen], không còn ở tầng này nữa.
 * - Tầng trong: xem [MainShellScreen] — NavDisplay riêng cho 5 tab, mỗi tab giữ
 *   backstack riêng.
 *
 * Route con nào đó muốn được add từ TRONG MainShellScreen (vd bấm "+ Thêm món
 * ăn" ở MealDiary) phải bubble ra qua `onNavigateOuter` để add vào ĐÚNG
 * backstack tầng ngoài này, không phải backstack riêng của tab hay của
 * MainShell.
 */
@Composable
private fun HealthTrackerNavHost(startRoute: Route) {
    val backStack = rememberNavBackStack(startRoute)

    // Toast dùng chung toàn app: bất kỳ ViewModel nào cũng gửi message được
    // (qua ToastController), hiện ở ĐÚNG 1 chỗ này (tầng NGOÀI CÙNG) vì đây là
    // nơi duy nhất sống xuyên suốt mọi lần chuyển màn Ở CẢ 2 TẦNG — kể cả khi
    // đang đứng ở FoodPicker/Settings (tầng ngoài) lẫn Dashboard/MealDiary (tầng
    // trong) — xem thêm ui/toast/ToastController.kt.
    val toastViewModel: ToastViewModel = hiltViewModel()
    // vì toast có thể null => phải type "T?", không phải mỗi "T"
    var currentToast by remember { mutableStateOf<ToastData?>(null) }

    LaunchedEffect(Unit) {
        toastViewModel.messages.collect { message ->
            // Chờ pop/push transition của NavDisplay chạy xong rồi mới hiện toast,
            // để enter animation không bị rớt frame vì main thread đang bận vẽ transition.
            delay(NAV_TRANSITION_DURATION_MS.toLong())
            // Giữ nguyên textRes, KHÔNG resolve String ở đây bằng context.getString():
            // LaunchedEffect(Unit) không bao giờ chạy lại nên context bị đóng băng ở
            // lần compose đầu tiên -> đổi ngôn ngữ ở Settings sau đó sẽ không còn tác
            // dụng với toast. Để SharedToast tự stringResource() lúc render.
            currentToast = ToastData(textRes = message.textRes, type = message.type)
            delay(3000)
            currentToast = null
        }
    }

    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                // KHÔNG áp `Modifier.padding(padding)` chung ở đây — MainShell cần
                // toàn bộ màn hình thật (nó có bottom bar riêng, tự lo insets của
                // chính nó). padding được áp RIÊNG cho từng route con khác bên dưới.
                modifier = Modifier.fillMaxSize(),
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
                        Box(modifier = Modifier.padding(padding)) {
                            OnboardingScreen(
                                onFinishOnboarding = {
                                    // Xoá backstack để không back về Onboarding được nữa.
                                    backStack.clear()
                                    backStack.add(Route.MainShell)
                                },
                            )
                        }
                    }

                    // ----- Shell 5-tab (bottom bar sống bên trong MainShellScreen) -----
                    entry<Route.MainShell> {
                        MainShellScreen(
                            onNavigateOuter = { route -> backStack.add(route) },
                        )
                    }

                    // ----- Màn con Meal (ẩn bottom bar) -----
                    entry<Route.FoodPicker> { route ->
                        Box(modifier = Modifier.padding(padding)) {
                            FoodPickerScreen(
                                mealType = route.mealType,
                                logDate = LocalDate.parse(route.logDate),
                                onBack = { backStack.removeLastOrNull() },
                                onEnterNewFood = { backStack.add(Route.EnterFoodManually()) },
                                onEditFood = { food -> backStack.add(Route.EnterFoodManually(food = food)) },
                            )
                        }
                    }
                    entry<Route.EnterFoodManually> { route ->
                        Box(modifier = Modifier.padding(padding)) {
                            EnterFoodManuallyScreen(
                                food = route.food,
                                onClose = { backStack.removeLastOrNull() },
                            )
                        }
                    }

                    // ----- Màn con Activity (ẩn bottom bar) -----
                    entry<Route.ChooseActivity> { route ->
                        Box(modifier = Modifier.padding(padding)) {
                            ActivityPickerScreen(
                                logDate = LocalDate.parse(route.logDate),
                                onBack = { backStack.removeLastOrNull() },
                                onEnterNewActivity = { backStack.add(Route.EnterActivityManually()) },
                                onEditActivity = { activity -> backStack.add(Route.EnterActivityManually(activity = activity)) },
                            )
                        }
                    }
                    entry<Route.EnterActivityManually> { route ->
                        Box(modifier = Modifier.padding(padding)) {
                            EnterActivityScreen(
                                activity = route.activity,
                                onClose = { backStack.removeLastOrNull() },
                            )
                        }
                    }

                    // ----- Màn con Profile (ẩn bottom bar) -----
                    entry<Route.Settings> {
                        Box(modifier = Modifier.padding(padding)) {
                            SettingScreen(
                                onBackClick = { backStack.removeLastOrNull() },
                                onResetComplete = {
                                    // Giống onFinishOnboarding: xoá sạch backstack, không cho back
                                    // trở lại MainShell/Settings vì hồ sơ đã bị xoá.
                                    backStack.clear()
                                    backStack.add(Route.Onboarding)
                                },
                            )
                        }
                    }
                    entry<Route.EditProfile> {
                        Box(modifier = Modifier.padding(padding)) {
                            EditProfileScreen(onBack = { backStack.removeLastOrNull() })
                        }
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
