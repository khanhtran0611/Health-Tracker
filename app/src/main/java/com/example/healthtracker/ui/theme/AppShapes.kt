package com.example.healthtracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class AppShapes(
    val small: Shape = RoundedCornerShape(8.dp),
    val medium: Shape = RoundedCornerShape(12.dp),
    val large: Shape = RoundedCornerShape(16.dp),
    val extraLarge: Shape = RoundedCornerShape(24.dp),
    val full: Shape = RoundedCornerShape(percent = 50),

    val segmentedControlInner: Shape = RoundedCornerShape(20.dp),
    val barTopCorner: Shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
)

val LocalAppShapes = staticCompositionLocalOf { AppShapes() }

val MaterialTheme.appShapes: AppShapes
    @Composable
    get() = LocalAppShapes.current
