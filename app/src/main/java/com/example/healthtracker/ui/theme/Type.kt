package com.example.healthtracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.healthtracker.domain.model.FontSize

fun appTypography(fontSize: FontSize): Typography {

    val scale = when (fontSize) {
        FontSize.SMALL -> 0.85f
        FontSize.MEDIUM -> 1.0f
        FontSize.LARGE -> 1.15f
    }
    return Typography(
        displayLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 45.sp * scale,
            lineHeight = 52.sp * scale,
        ),
        headlineLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp * scale,
            lineHeight = 36.sp * scale,
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp * scale,
            lineHeight = 28.sp * scale,
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp * scale,
            lineHeight = 24.sp * scale,
            letterSpacing = 0.5.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp * scale,
            lineHeight = 20.sp * scale,
            letterSpacing = 0.25.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp * scale,
            lineHeight = 20.sp * scale,
            letterSpacing = 0.1.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp * scale,
            lineHeight = 16.sp * scale,
            letterSpacing = 0.5.sp,
        ),
    )
}
