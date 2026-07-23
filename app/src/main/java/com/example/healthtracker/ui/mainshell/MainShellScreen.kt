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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.healthtracker.ui.activity.activitydiary.ActivityDiaryScreen
import com.example.healthtracker.ui.dashboard.DashboardScreen
import com.example.healthtracker.ui.meal.mealdiary.MealDiaryScreen
import com.example.healthtracker.ui.navigation.Route
import com.example.healthtracker.ui.profile.ProfileScreen
import com.example.healthtracker.ui.stats.StatisticsScreen

private const val TAB_TRANSITION_DURATION_MS = 300

@Composable
fun MainShellScreen(onNavigateOuter: (Route) -> Unit) {
    val tabBackStacks = rememberTabBackStacks()

    var currentTabIndex by rememberSaveable { mutableIntStateOf(TopLevelTab.DASHBOARD.ordinal) }
    val currentTab = TopLevelTab.entries[currentTabIndex]
    val activeBackStack = tabBackStacks.getValue(currentTab.route)

    var isMovingForward by remember { mutableStateOf(true) }

    fun switchToTab(tab: TopLevelTab) {
        isMovingForward = tab.ordinal >= currentTabIndex
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

            transitionSpec = {
                val towards = if (isMovingForward) {
                    AnimatedContentTransitionScope.SlideDirection.Start
                } else {
                    AnimatedContentTransitionScope.SlideDirection.End
                }
                slideIntoContainer(
                    towards = towards,
                    animationSpec = tween(TAB_TRANSITION_DURATION_MS),
                ) + fadeIn(animationSpec = tween(TAB_TRANSITION_DURATION_MS)) togetherWith
                    slideOutOfContainer(
                        towards = towards,
                        animationSpec = tween(TAB_TRANSITION_DURATION_MS),
                    ) + fadeOut(animationSpec = tween(TAB_TRANSITION_DURATION_MS))
            },
            popTransitionSpec = {
                val towards = if (isMovingForward) {
                    AnimatedContentTransitionScope.SlideDirection.Start
                } else {
                    AnimatedContentTransitionScope.SlideDirection.End
                }
                slideIntoContainer(
                    towards = towards,
                    animationSpec = tween(TAB_TRANSITION_DURATION_MS),
                ) + fadeIn(animationSpec = tween(TAB_TRANSITION_DURATION_MS)) togetherWith
                    slideOutOfContainer(
                        towards = towards,
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
                entry<Route.Stats> { StatisticsScreen() }
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
