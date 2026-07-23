package com.example.healthtracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BorderWidths(
    val borderNone: Dp = 0.dp,
    val borderThin: Dp = 1.dp,
    val borderMedium: Dp = 1.5.dp,
    val borderThick: Dp = 2.dp,
)

val LocalBorderWidths = staticCompositionLocalOf { BorderWidths() }

val MaterialTheme.borderWidths: BorderWidths
    @Composable
    get() = LocalBorderWidths.current
