package com.example.healthtracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val none: Dp = 0.dp,
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val xsPlus: Dp = 6.dp,
    val sm: Dp = 8.dp,
    val smPlus: Dp = 10.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val lgPlus: Dp = 19.dp,
    val xlMinus: Dp = 20.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    val xxxl: Dp = 48.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    get() = LocalSpacing.current
