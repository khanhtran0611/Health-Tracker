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
import androidx.compose.material3.MaterialTheme
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
import com.example.healthtracker.ui.activity.activitypicker.ActivityPickerScreen
import com.example.healthtracker.ui.activity.enteractivity.EnterActivityScreen
import com.example.healthtracker.ui.component.SplashScreen
import com.example.healthtracker.ui.mainshell.MainShellScreen
import com.example.healthtracker.ui.meal.enterfood.EnterFoodManuallyScreen
import com.example.healthtracker.ui.meal.foodpicker.FoodPickerScreen
import com.example.healthtracker.ui.onboarding.OnboardingScreen
import com.example.healthtracker.ui.profile.EditProfileScreen
import com.example.healthtracker.ui.settings.SettingScreen
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing
import com.example.healthtracker.ui.toast.SharedToast
import com.example.healthtracker.ui.toast.ToastData
import com.example.healthtracker.ui.toast.ToastViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

private const val NAV_TRANSITION_DURATION_MS = 300

@Composable
fun HealthTrackerApp() {
    val appStartViewModel: AppStartViewModel = hiltViewModel()
    val startDestination by appStartViewModel.startDestination.collectAsStateWithLifecycle()

    when (startDestination) {
        AppStartDestination.LOADING -> SplashScreen()
        AppStartDestination.ONBOARDING -> HealthTrackerNavHost(startRoute = Route.Onboarding)
        AppStartDestination.MAIN_SHELL -> HealthTrackerNavHost(startRoute = Route.MainShell)
    }
}

@Composable
private fun HealthTrackerNavHost(startRoute: Route) {
    val backStack = rememberNavBackStack(startRoute)

    val toastViewModel: ToastViewModel = hiltViewModel()

    var currentToast by remember { mutableStateOf<ToastData?>(null) }

    LaunchedEffect(Unit) {
        toastViewModel.messages.collect { message ->

            delay(NAV_TRANSITION_DURATION_MS.toLong())

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

                modifier = Modifier.fillMaxSize(),

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

                    entry<Route.Onboarding> {
                        Box(modifier = Modifier.padding(padding)) {
                            OnboardingScreen(
                                onFinishOnboarding = {

                                    backStack.clear()
                                    backStack.add(Route.MainShell)
                                },
                            )
                        }
                    }

                    entry<Route.MainShell> {
                        MainShellScreen(
                            onNavigateOuter = { route -> backStack.add(route) },
                        )
                    }

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

                    entry<Route.Settings> {
                        Box(modifier = Modifier.padding(padding)) {
                            SettingScreen(
                                onBackClick = { backStack.removeLastOrNull() },
                                onResetComplete = {

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
            val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
                MaterialTheme.sizing.topBarHeight + MaterialTheme.spacing.sm
            SharedToast(
                toastData = currentToast,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = topPadding)
            )
        }
    }
}
