package com.example.healthtracker.ui.stats.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.stats.DailyCalorieStat
import kotlin.math.roundToInt

@Composable
fun WeeklySummaryCard(
    dailyStats: List<DailyCalorieStat>,
    avgEatenPerDay: Double,
    avgBurnedPerDay: Double,
    daysGoalMet: Int,
    tdee: Double,
    modifier: Modifier = Modifier,
) {
    val kcalUnit = stringResource(R.string.unit_kcal)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.stats_summary_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))

            SummaryRow(
                label = stringResource(R.string.stats_avg_eaten_label),
                value = "${avgEatenPerDay.roundToInt()} $kcalUnit",
                valueColor = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))

            SummaryRow(
                label = stringResource(R.string.stats_avg_burned_label),
                value = "${avgBurnedPerDay.roundToInt()} $kcalUnit",
                valueColor = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            SummaryRow(
                label = stringResource(R.string.stats_days_goal_met_label),
                value = stringResource(R.string.stats_days_goal_met_value, daysGoalMet, dailyStats.size),
                valueColor = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                dailyStats.forEach { stat ->
                    val dotColor = if (stat.isGoalMet(tdee)) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                    Box(modifier = Modifier.size(12.dp).background(dotColor, CircleShape))
                }
            }
        }
    }
}

@Composable
internal fun SummaryRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor,
        )
    }
}
