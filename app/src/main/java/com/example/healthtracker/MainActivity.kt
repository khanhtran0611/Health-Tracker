package com.example.healthtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.ui.locale.LocalizedApp
import com.example.healthtracker.ui.navigation.AppStartDestination
import com.example.healthtracker.ui.navigation.AppStartViewModel
import com.example.healthtracker.ui.navigation.HealthTrackerApp
import com.example.healthtracker.ui.settings.SettingsViewModel
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appStartViewModel: AppStartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            appStartViewModel.startDestination.value == AppStartDestination.LOADING
        }

        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

            LocalizedApp(language = uiState.settings.language) {
                HealthTrackerTheme(
                    themePreset = uiState.settings.themePreset,
                    brightness = uiState.settings.brightness,
                    fontSize = uiState.settings.fontSize,
                ) {
                    HealthTrackerApp()
                }
            }
        }
    }
}
