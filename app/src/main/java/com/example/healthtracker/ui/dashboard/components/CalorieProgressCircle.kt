package com.example.healthtracker.ui.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthtracker.R
import com.example.healthtracker.ui.dashboard.CalorieStatus
import kotlin.math.abs

@Composable
fun CalorieProgressCircle(
    caloriesLeft: Int,
    progress: Float,
    calorieStatus: CalorieStatus,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        val trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
        // Vượt quá (remaining < 0) -> đỏ; vừa đủ (remaining == 0) -> xanh lá;
        // còn dư (remaining > 0) -> cam như mặc định. Cung progress lúc vượt
        // quá đã full 360° (coerce bên dưới) nên đè kín track xám, "cả vòng
        // tròn" sẽ hiện đúng màu như yêu cầu.
        val progressColor = when (calorieStatus) {
            CalorieStatus.OVER_TARGET -> MaterialTheme.colorScheme.error
            CalorieStatus.ON_TARGET -> MaterialTheme.colorScheme.primary
            CalorieStatus.UNDER_TARGET -> MaterialTheme.colorScheme.tertiary
        }
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
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // Draw progress track
            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle * clampedProgress,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            // caloriesLeft âm nghĩa là đã vượt ngân sách -> hiện trị tuyệt đối +
            // đổi nhãn "kcal left" thành "kcal over", KHÔNG hiện dấu trừ trần trụi
            // (vd "-150") vì user thường không hiểu ngay đó là "vượt bao nhiêu".
            Text(
                text = "${abs(caloriesLeft)}",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = when (calorieStatus) {
                    CalorieStatus.OVER_TARGET -> MaterialTheme.colorScheme.error
                    CalorieStatus.ON_TARGET -> MaterialTheme.colorScheme.primary
                    CalorieStatus.UNDER_TARGET -> MaterialTheme.colorScheme.tertiary
                }
            )
            Text(
                text = stringResource(
                    if (calorieStatus == CalorieStatus.OVER_TARGET) R.string.unit_kcal_over else R.string.unit_kcal_left
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
