package com.example.healthtracker.ui.stats.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.stats.DailyCalorieStat
import kotlin.math.roundToInt

private val CHART_HEIGHT = 180.dp
private val Y_AXIS_WIDTH = 40.dp
private const val Y_LABEL_COUNT = 5

@Composable
fun WeeklyTrendChartCard(dailyStats: List<DailyCalorieStat>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.stats_line_chart_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))

            val maxY = (dailyStats.maxOfOrNull { it.eaten } ?: 0.0).coerceAtLeast(1.0)
            val yLabels = (Y_LABEL_COUNT - 1 downTo 0).map { step -> (maxY * step / (Y_LABEL_COUNT - 1)).roundToInt() }
            val points = dailyStats.map { (it.eaten / maxY).toFloat().coerceIn(0f, 1f) }

            Box(modifier = Modifier.fillMaxWidth().height(CHART_HEIGHT)) {
                Column(
                    modifier = Modifier.fillMaxHeight().width(Y_AXIS_WIDTH),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    yLabels.forEach { label ->
                        Text(
                            text = label.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.offset(y = (-6).dp),
                        )
                    }
                }

                TrendLine(
                    points = points,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = Y_AXIS_WIDTH),
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
                strokeWidth = 1.dp.toPx(),
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
        drawPath(path = path, color = lineColor, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))

        coords.forEach { point ->
            drawCircle(color = lineColor, radius = 6.dp.toPx(), center = point)
            drawCircle(color = Color.White, radius = 4.dp.toPx(), center = point)
        }
    }
}
