package com.example.healthtracker.ui.stats.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.healthtracker.R
import com.example.healthtracker.ui.component.formatting.formatWeekdayShort
import com.example.healthtracker.ui.stats.DailyCalorieStat
import java.time.LocalDate
import kotlin.math.roundToInt
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun WeeklyBarChartCard(dailyStats: List<DailyCalorieStat>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.lg)) {
            Text(
                text = stringResource(R.string.stats_bar_chart_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

            val maxEaten = (dailyStats.maxOfOrNull { it.eaten } ?: 0.0).coerceAtLeast(1.0)
            val today = LocalDate.now()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.sizing.barChartHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                dailyStats.forEach { stat ->
                    DayBar(
                        stat = stat,
                        maxEaten = maxEaten,
                        isToday = stat.date == today,
                    )
                }
            }
        }
    }
}

@Composable
private fun DayBar(stat: DailyCalorieStat, maxEaten: Double, isToday: Boolean, modifier: Modifier = Modifier) {
    val heightRatio = (stat.eaten / maxEaten).toFloat().coerceIn(0f, 1f)

    val animatedHeightRatio by animateFloatAsState(
        targetValue = heightRatio,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "barHeight",
    )
    val barColor = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stat.eaten.roundToInt().toString(),
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xs))
        Box(
            modifier = Modifier
                .weight(1f)
                .width(MaterialTheme.sizing.barChartBarWidth),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(animatedHeightRatio)
                    .clip(MaterialTheme.appShapes.barTopCorner)
                    .background(if (heightRatio > 0f) barColor else Color.Transparent),
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))
        Text(
            text = formatWeekdayShort(stat.date),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
        )
    }
}
