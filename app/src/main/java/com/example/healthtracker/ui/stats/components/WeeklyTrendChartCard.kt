package com.example.healthtracker.ui.stats.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.healthtracker.R
import com.example.healthtracker.ui.stats.DailyCalorieStat
import kotlin.math.roundToInt
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

private const val Y_LABEL_COUNT = 5

@Composable
fun WeeklyTrendChartCard(dailyStats: List<DailyCalorieStat>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.lg)) {
            Text(
                text = stringResource(R.string.stats_line_chart_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

            val maxY = (dailyStats.maxOfOrNull { it.eaten } ?: 0.0).coerceAtLeast(1.0)
            val yLabels = (Y_LABEL_COUNT - 1 downTo 0).map { step -> (maxY * step / (Y_LABEL_COUNT - 1)).roundToInt() }
            val points = dailyStats.map { (it.eaten / maxY).toFloat().coerceIn(0f, 1f) }

            val animatedPoints = points.map { target ->
                val animated by animateFloatAsState(
                    targetValue = target,
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
                    label = "trendPoint",
                )
                animated
            }

            Box(modifier = Modifier.fillMaxWidth().height(MaterialTheme.sizing.trendChartHeight)) {
                Column(
                    modifier = Modifier.fillMaxHeight().width(MaterialTheme.sizing.trendChartYAxisWidth),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    yLabels.forEach { label ->
                        Text(
                            text = label.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.offset(y = -MaterialTheme.sizing.chartAxisLabelOffset),
                        )
                    }
                }

                TrendLine(
                    points = animatedPoints,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = MaterialTheme.sizing.trendChartYAxisWidth),
                )
            }
        }
    }
}

@Composable
private fun TrendLine(points: List<Float>, modifier: Modifier = Modifier) {
    if (points.size < 2) return

    val lineColor = MaterialTheme.colorScheme.primary
    val fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    val gridLineWidth = MaterialTheme.borderWidths.borderThin
    val lineWidth = MaterialTheme.sizing.chartLineWidth
    val pointOuterRadius = MaterialTheme.sizing.chartPointOuterRadius
    val pointInnerRadius = MaterialTheme.sizing.chartPointInnerRadius

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val stepY = height / (Y_LABEL_COUNT - 1)
        for (i in 0 until Y_LABEL_COUNT) {
            val y = i * stepY
            drawLine(
                color = lineColor.copy(alpha = 0.15f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = gridLineWidth.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
            )
        }

        val xStep = width / (points.size - 1)
        val coords = points.mapIndexed { index, value -> Offset(index * xStep, height * (1 - value)) }

        val path = Path().apply {
            moveTo(coords[0].x, coords[0].y)
            for (i in 0 until coords.size - 1) {
                val p1 = coords[i]
                val p2 = coords[i + 1]
                val controlX = (p1.x + p2.x) / 2
                cubicTo(controlX, p1.y, controlX, p2.y, p2.x, p2.y)
            }
        }

        val fillPath = Path().apply {
            addPath(path)
            lineTo(coords.last().x, height)
            lineTo(coords.first().x, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(colors = listOf(fillColor, Color.Transparent), startY = 0f, endY = height),
        )
        drawPath(path = path, color = lineColor, style = Stroke(width = lineWidth.toPx(), cap = StrokeCap.Round))

        coords.forEach { point ->
            drawCircle(color = lineColor, radius = pointOuterRadius.toPx(), center = point)
            drawCircle(color = Color.White, radius = pointInnerRadius.toPx(), center = point)
        }
    }
}
