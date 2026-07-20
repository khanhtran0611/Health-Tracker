package com.example.healthtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.ui.navigation.HealthTrackerApp
import com.example.healthtracker.ui.theme.AppThemeViewModel
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: AppThemeViewModel = hiltViewModel()
            val appSettings by themeViewModel.appSettings.collectAsStateWithLifecycle()

            HealthTrackerTheme(
                themePreset = appSettings.themePreset,
                brightness = appSettings.brightness,
            ) {
                HealthTrackerApp()
            }
        }
    }
}
