package com.example.healthtracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Elevation(
    val flat: Dp = 0.dp,
    val listItem: Dp = 1.dp,
    val entryCard: Dp = 2.dp,
    val toast: Dp = 8.dp,
)

val LocalElevation = staticCompositionLocalOf { Elevation() }

val MaterialTheme.elevation: Elevation
    @Composable
    get() = LocalElevation.current
