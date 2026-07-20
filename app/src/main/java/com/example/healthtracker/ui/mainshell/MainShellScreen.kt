package com.example.healthtracker.ui.mainshell

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.healthtracker.ui.activitydiary.ActivityDiaryScreen
import com.example.healthtracker.ui.component.PlaceholderScreen
import com.example.healthtracker.ui.dashboard.DashboardScreen
import com.example.healthtracker.ui.mealdiary.MealDiaryScreen
import com.example.healthtracker.ui.navigation.Route
import com.example.healthtracker.ui.profile.ProfileScreen

// Thời lượng animation đổi tab (slide + fade) — giống hệt NavDisplay tầng
// ngoài ở HealthTrackerApp.kt, để cảm giác chuyển màn nhất quán ở cả 2 tầng.
private const val TAB_TRANSITION_DURATION_MS = 300

/**
 * Shell 5-tab: `Scaffold` với `bottomBar` là [NavigationBar], bên trong là 1
 * NavDisplay TẦNG TRONG riêng chỉ chứa route của 5 tab (Dashboard/MealDiary/
 * ActivityDiary/Stats/Profile). Mỗi tab giữ backstack riêng (xem
 * [rememberTabBackStacks]) — đổi tab CHỈ đổi backstack nào đang được NavDisplay
 * hiển thị, KHÔNG xoá/tạo lại entry, nên state/scroll của tab không bị mất khi
 * quay lại.
 *
 * QUAN TRỌNG về insets: route [Route.MainShell] được entry ở NavDisplay tầng
 * ngoài (`HealthTrackerApp.kt`) KHÔNG bọc `Modifier.padding(padding)` như các
 * route con khác — vì màn này có `bottomBar` CỦA RIÊNG NÓ, phải nằm sát đáy
 * màn hình thật. Nếu bị ép vào phần padding đã trừ sẵn insets hệ thống (như
 * FoodPicker/Settings...), NavigationBar sẽ lơ lửng phía trên 1 khoảng trống
 * thay vì nằm sát đáy. Vì vậy `Scaffold` ở đây dùng insets MẶC ĐỊNH (không zero
 * ra như các Scaffold con khác), tự lo toàn bộ status bar/nav bar của chính nó.
 *
 * [onNavigateOuter] bubble các navigate ra ngoài phạm vi 5 tab (FoodPicker,
 * ChooseActivity, Settings, EditProfile...) lên backstack TẦNG NGOÀI —
 * MainShellScreen không tự thêm những route đó vào backstack của tab nào cả.
 */
@Composable
fun MainShellScreen(onNavigateOuter: (Route) -> Unit) {
    val tabBackStacks = rememberTabBackStacks()

    // rememberSaveable (không phải remember thường) vì entry MainShell có thể bị
    // dispose khi push FoodPicker/Settings... lên backstack tầng ngoài rồi pop
    // trở lại — phải nhớ ĐANG Ở TAB NÀO qua lần dispose/restore đó, không được
    // reset về tab Dashboard mỗi lần quay lại từ 1 màn con.
    var currentTabIndex by rememberSaveable { mutableIntStateOf(TopLevelTab.DASHBOARD.ordinal) }
    val currentTab = TopLevelTab.entries[currentTabIndex]
    val activeBackStack = tabBackStacks.getValue(currentTab.route)

    // Dùng chung cho cả bottom nav lẫn shortcut trong Dashboard (Add meal/Add
    // activity) — mọi nơi muốn nhảy sang 1 trong 5 tab đều gọi qua đây.
    fun switchToTab(tab: TopLevelTab) {
        navigateToTab(tabBackStacks.getValue(tab.route), tab.route)
        currentTabIndex = tab.ordinal
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                TopLevelTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { switchToTab(tab) },
                        icon = { Icon(tab.icon, contentDescription = stringResource(tab.labelRes)) },
                        label = { Text(
                            text = stringResource(tab.labelRes),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                                },
                    )
                }
            }
        },
    ) { padding ->
        NavDisplay(
            backStack = activeBackStack,
            onBack = { activeBackStack.removeLastOrNull() },
            modifier = Modifier.fillMaxSize().padding(padding),
            // Đổi tab: slide + fade y hệt NavDisplay tầng ngoài ở HealthTrackerApp.kt.
            transitionSpec = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(TAB_TRANSITION_DURATION_MS),
                ) + fadeIn(animationSpec = tween(TAB_TRANSITION_DURATION_MS)) togetherWith
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(TAB_TRANSITION_DURATION_MS),
                    ) + fadeOut(animationSpec = tween(TAB_TRANSITION_DURATION_MS))
            },
            popTransitionSpec = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(TAB_TRANSITION_DURATION_MS),
                ) + fadeIn(animationSpec = tween(TAB_TRANSITION_DURATION_MS)) togetherWith
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(TAB_TRANSITION_DURATION_MS),
                    ) + fadeOut(animationSpec = tween(TAB_TRANSITION_DURATION_MS))
            },
            entryProvider = entryProvider {
                entry<Route.Dashboard> {
                    DashboardScreen(
                        onAddMealClick = { switchToTab(TopLevelTab.MEAL) },
                        onAddActivityClick = { switchToTab(TopLevelTab.ACTIVITY) }
                    )
                }
                entry<Route.MealDiary> {
                    MealDiaryScreen(
                        onAddFood = { mealType, logDate ->
                            onNavigateOuter(Route.FoodPicker(mealType = mealType, logDate = logDate.toString()))
                        },
                    )
                }
                entry<Route.ActivityDiary> {
                    ActivityDiaryScreen(
                        onAddActivity = { date ->
                            onNavigateOuter(Route.ChooseActivity(logDate = date.toString()))
                        },
                    )
                }
                entry<Route.Stats> { PlaceholderScreen("Stats") }
                entry<Route.Profile> {
                    ProfileScreen(
                        onEditProfileClick = { onNavigateOuter(Route.EditProfile) },
                        onSettingsClick = { onNavigateOuter(Route.Settings) },
                    )
                }
            },
        )
    }
}
