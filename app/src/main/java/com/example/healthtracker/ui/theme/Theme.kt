package com.example.healthtracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/** Vitality Material — theme sáng, xem docs/design-system.md */
private val LightColorScheme = lightColorScheme(
    primary = VitalityPrimary,
    onPrimary = VitalityOnPrimary,
    primaryContainer = VitalityPrimaryContainer,
    onPrimaryContainer = VitalityOnPrimaryContainer,
    inversePrimary = VitalityInversePrimary,
    secondary = VitalitySecondary,
    onSecondary = VitalityOnSecondary,
    secondaryContainer = VitalitySecondaryContainer,
    onSecondaryContainer = VitalityOnSecondaryContainer,
    tertiary = VitalityTertiary,
    onTertiary = VitalityOnTertiary,
    tertiaryContainer = VitalityTertiaryContainer,
    onTertiaryContainer = VitalityOnTertiaryContainer,
    error = VitalityError,
    onError = VitalityOnError,
    errorContainer = VitalityErrorContainer,
    onErrorContainer = VitalityOnErrorContainer,
    background = VitalityBackground,
    onBackground = VitalityOnBackground,
    surface = VitalitySurface,
    onSurface = VitalityOnSurface,
    surfaceVariant = VitalitySurfaceVariant,
    onSurfaceVariant = VitalityOnSurfaceVariant,
    outline = VitalityOutline,
    outlineVariant = VitalityOutlineVariant,
    inverseSurface = VitalityInverseSurface,
    inverseOnSurface = VitalityInverseOnSurface,
    surfaceDim = VitalitySurfaceDim,
    surfaceBright = VitalitySurfaceBright,
    surfaceContainerLowest = VitalitySurfaceContainerLowest,
    surfaceContainerLow = VitalitySurfaceContainerLow,
    surfaceContainer = VitalitySurfaceContainer,
    surfaceContainerHigh = VitalitySurfaceContainerHigh,
    surfaceContainerHighest = VitalitySurfaceContainerHighest,
)

/** Obsidian — theme tối, xem docs/design-system2.md */
private val DarkColorScheme = darkColorScheme(
    primary = ObsidianPrimary,
    onPrimary = ObsidianOnPrimary,
    primaryContainer = ObsidianPrimaryContainer,
    onPrimaryContainer = ObsidianOnPrimaryContainer,
    secondary = ObsidianSecondary,
    onSecondary = ObsidianOnSecondary,
    secondaryContainer = ObsidianSecondaryContainer,
    onSecondaryContainer = ObsidianOnSecondaryContainer,
    tertiary = ObsidianTertiary,
    onTertiary = ObsidianOnTertiary,
    tertiaryContainer = ObsidianTertiaryContainer,
    onTertiaryContainer = ObsidianOnTertiaryContainer,
    error = ObsidianError,
    onError = ObsidianOnError,
    errorContainer = ObsidianErrorContainer,
    onErrorContainer = ObsidianOnErrorContainer,
    background = ObsidianBackground,
    onBackground = ObsidianOnBackground,
    surface = ObsidianSurface,
    onSurface = ObsidianOnSurface,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = ObsidianOnSurfaceVariant,
    outline = ObsidianOutline,
    outlineVariant = ObsidianOutlineVariant,
    surfaceDim = ObsidianSurfaceDim,
    surfaceBright = ObsidianSurfaceBright,
    surfaceContainerLowest = ObsidianSurfaceContainerLowest,
    surfaceContainerLow = ObsidianSurfaceContainerLow,
    surfaceContainer = ObsidianSurfaceContainer,
    surfaceContainerHigh = ObsidianSurfaceContainerHigh,
    surfaceContainerHighest = ObsidianSurfaceContainerHighest,
)

@Composable
fun HealthTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // App có bộ nhận diện thương hiệu riêng (Vitality/Obsidian) nên mặc định TẮT
    // dynamic color — không để Material You lấy màu từ hình nền đè lên brand.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
