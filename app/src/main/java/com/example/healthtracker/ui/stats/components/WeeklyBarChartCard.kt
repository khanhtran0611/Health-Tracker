package com.example.healthtracker.ui.stats.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.component.formatWeekdayShort
import com.example.healthtracker.ui.stats.DailyCalorieStat
import java.time.LocalDate
import kotlin.math.roundToInt

private val CHART_HEIGHT = 200.dp
private val BAR_WIDTH = 32.dp

@Composable
fun WeeklyBarChartCard(dailyStats: List<DailyCalorieStat>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.stats_bar_chart_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))

            val maxEaten = (dailyStats.maxOfOrNull { it.eaten } ?: 0.0).coerceAtLeast(1.0)
            val today = LocalDate.now()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CHART_HEIGHT),
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

/**
 * Cột 1 ngày: nhãn calo, thanh cột, nhãn thứ. Thanh cột nằm trong 1 Box chiều
 * cao cố định (weight(1f)) rồi mới fillMaxHeight(ratio) bên trong + căn
 * BottomCenter — nhờ vậy đáy mọi cột luôn thẳng hàng bất kể nhãn phía trên/dưới
 * có chiều cao khác nhau, thay vì fillMaxHeight(ratio) tính theo chiều cao cả
 * Column (dễ lệch khi nội dung tràn quá CHART_HEIGHT).
 */
@Composable
private fun DayBar(stat: DailyCalorieStat, maxEaten: Double, isToday: Boolean, modifier: Modifier = Modifier) {
    val heightRatio = (stat.eaten / maxEaten).toFloat().coerceIn(0f, 1f)
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
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .width(BAR_WIDTH),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(heightRatio)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(if (heightRatio > 0f) barColor else Color.Transparent),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formatWeekdayShort(stat.date),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
        )
    }
}
