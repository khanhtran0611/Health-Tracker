package com.example.healthtracker.ui.dashboard.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

        val progressColor = when (calorieStatus) {
            CalorieStatus.OVER_TARGET -> MaterialTheme.colorScheme.error
            CalorieStatus.ON_TARGET -> MaterialTheme.colorScheme.primary
            CalorieStatus.UNDER_TARGET -> MaterialTheme.colorScheme.tertiary
        }
        val strokeWidth = 24.dp

        val clampedProgress = progress.coerceIn(0f, 1f)

        val animatedProgress by animateFloatAsState(
            targetValue = clampedProgress,
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            label = "calorieProgress",
        )

        Canvas(
            modifier = Modifier.size(240.dp)
        ) {

            val startAngle = -90f
            val sweepAngle = 360f
            val inset = strokeWidth.toPx() / 2
            val arcSize = size.copy(width = size.width - strokeWidth.toPx(), height = size.height - strokeWidth.toPx())

            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle * animatedProgress,
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

            val animatedCaloriesLeft by animateIntAsState(
                targetValue = abs(caloriesLeft),
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
                label = "calorieCountUp",
            )
            Text(
                text = "$animatedCaloriesLeft",
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
