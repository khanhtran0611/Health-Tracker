package com.example.healthtracker.ui.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalorieProgressCircle(
    caloriesLeft: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        val trackColor = Color(0xFFE0E0E0)
        // progress > 1f nghĩa là đã ăn vượt quá ngân sách thật (remaining < 0,
        // đã tính cả calo đốt — xem DashboardUiState.progress) -> tô đỏ cả
        // vòng tròn thay vì cam. Cung progress lúc này full 360° (coerce bên
        // dưới) nên đè kín track xám, "cả vòng tròn" sẽ hiện đỏ như yêu cầu.
        val progressColor = if (progress > 1f) Color(0xFFE53935) else Color(0xFFFF7043)
        val strokeWidth = 24.dp
        
        Canvas(
            modifier = Modifier.size(240.dp)
        ) {
            // -90f = đỉnh 12 giờ (quy ước Compose: 0f = 3 giờ, góc tăng theo chiều kim đồng hồ).
            val startAngle = -90f
            val sweepAngle = 360f
            // Chặn progress trong [0f, 1f] — user ăn vượt TDEE (progress > 1) không được
            // làm cung tô tràn quá 1 vòng tròn.
            val clampedProgress = progress.coerceIn(0f, 1f)
            val inset = strokeWidth.toPx() / 2
            val arcSize = size.copy(width = size.width - strokeWidth.toPx(), height = size.height - strokeWidth.toPx())
            
            // Draw background track
            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            
            // Draw progress track
            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle * clampedProgress,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "$caloriesLeft",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1C2B33) // Very dark blue/grey
            )
            Text(
                text = "kcal left",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
