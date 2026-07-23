package com.example.healthtracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Sizing(
    val iconSmall: Dp = 16.dp,
    val iconMedium: Dp = 20.dp,
    val iconLarge: Dp = 24.dp,
    val progressRingThickness: Dp = 24.dp,

    val accentBarWidth: Dp = 4.dp,
    val progressBarHeight: Dp = 8.dp,
    val legendDotSize: Dp = 12.dp,
    val stepperIconSize: Dp = 18.dp,
    val bannerIconSize: Dp = 32.dp,
    val statDividerHeight: Dp = 32.dp,
    val iconContainerSize: Dp = 36.dp,
    val quantityStepperButtonSize: Dp = 36.dp,
    val avatarSize: Dp = 40.dp,
    val selectionIconContainerSize: Dp = 40.dp,
    val colorSwatchSize: Dp = 44.dp,
    val touchTarget: Dp = 48.dp,
    val buttonHeight: Dp = 56.dp,
    val splashLogoSize: Dp = 140.dp,
    val progressRingSize: Dp = 240.dp,

    val dashedCardCornerRadius: Dp = 16.dp,
    val chartLineWidth: Dp = 3.dp,
    val chartPointOuterRadius: Dp = 6.dp,
    val chartPointInnerRadius: Dp = 4.dp,
    val chartAxisLabelOffset: Dp = 6.dp,
    val bmiThumbSize: Dp = 16.dp,
    val topBarHeight: Dp = 64.dp,
    val barChartHeight: Dp = 200.dp,
    val barChartBarWidth: Dp = 32.dp,
    val trendChartHeight: Dp = 180.dp,
    val trendChartYAxisWidth: Dp = 40.dp,
)

val LocalSizing = staticCompositionLocalOf { Sizing() }

val MaterialTheme.sizing: Sizing
    @Composable
    get() = LocalSizing.current
