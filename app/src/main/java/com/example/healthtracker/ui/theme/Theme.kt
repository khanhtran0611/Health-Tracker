package com.example.healthtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.healthtracker.domain.model.Brightness
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.ThemePreset
import com.materialkolor.rememberDynamicColorScheme

@Composable
fun HealthTrackerTheme(
    themePreset: ThemePreset = ThemePreset.VITALITY_MATERIAL,
    brightness: Brightness = Brightness.SYSTEM,
    fontSize: FontSize = FontSize.MEDIUM,
    content: @Composable () -> Unit
) {
    val isDark = when (brightness) {
        Brightness.LIGHT -> false
        Brightness.DARK -> true
        Brightness.SYSTEM -> isSystemInDarkTheme()
    }
    val colorScheme = rememberDynamicColorScheme(
        seedColor = themePreset.seedColor,
        isDark = isDark,
        isAmoled = false,
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography(fontSize),
        content = content
    )
}
