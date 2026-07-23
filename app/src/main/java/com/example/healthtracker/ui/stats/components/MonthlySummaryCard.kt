package com.example.healthtracker.ui.stats.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import kotlin.math.roundToInt
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.spacing

@Composable
fun MonthlySummaryCard(
    avgEatenPerDay: Double,
    avgBurnedPerDay: Double,
    daysGoalMet: Int,
    totalDays: Int,
    modifier: Modifier = Modifier,
) {
    val kcalUnit = stringResource(R.string.unit_kcal)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.lg)) {
            Text(
                text = stringResource(R.string.stats_monthly_summary_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

            SummaryRow(
                label = stringResource(R.string.stats_avg_eaten_label),
                value = "${avgEatenPerDay.roundToInt()} $kcalUnit",
                valueColor = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

            SummaryRow(
                label = stringResource(R.string.stats_avg_burned_label),
                value = "${avgBurnedPerDay.roundToInt()} $kcalUnit",
                valueColor = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

            SummaryRow(
                label = stringResource(R.string.stats_days_goal_met_label),
                value = stringResource(R.string.stats_days_goal_met_value, daysGoalMet, totalDays),
                valueColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
