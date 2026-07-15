package com.example.healthtracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Type scale lấy từ token `typography` trong docs/design-system.md (font Inter).
 * Dùng chung cho cả 2 theme (sáng/tối) — chỉ màu sắc đổi theo theme, không đổi font.
 *
 * Chưa có file font Inter (.ttf) trong res/font nên tạm dùng FontFamily.Default
 * (font hệ thống). Khi có file Inter, chỉ cần đổi FontFamily.Default ở đây.
 *
 * displayLarge dùng số đo của token "stat-lg" (45sp) thay vì "display-lg" gốc (57sp):
 * doc liệt kê 2 token này thay thế cho nhau để hiển thị số liệu lớn (vd remaining
 * calories), và 45sp phù hợp màn hình điện thoại hơn.
 * headlineLarge dùng "headline-lg-mobile" (28sp) thay vì bản desktop (32sp) vì app
 * chỉ nhắm điện thoại.
 * Các slot không có token tương ứng trong doc (displayMedium/Small, headlineMedium/Small,
 * titleMedium/Small, bodySmall, labelSmall) giữ nguyên mặc định của Material3.
 */
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
