package com.example.healthtracker.ui.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Goal
import kotlin.math.roundToInt
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun ProfileStatsRow(
    weightKg: Double,
    heightCm: Double,
    goal: Goal,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {
        StatCard(
            title = stringResource(R.string.field_weight),
            value = "${roundTo1Decimal(weightKg)}",
            unit = stringResource(R.string.unit_kg),
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
        StatCard(
            title = stringResource(R.string.field_height),
            value = "${roundTo1Decimal(heightCm)}",
            unit = stringResource(R.string.unit_cm),
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
        GoalCard(goal = goal, modifier = Modifier.weight(1f).fillMaxHeight())
    }
}

private fun roundTo1Decimal(value: Double): Double = (value * 10).roundToInt() / 10.0

@Composable
private fun StatCard(title: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = MaterialTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xs))
            Row {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.alignByBaseline(),
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.xxs))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.alignByBaseline(),
                )
            }
        }
    }
}

@Composable
private fun GoalCard(goal: Goal, modifier: Modifier = Modifier) {
    val goalLabelRes = when (goal) {
        Goal.LOSE -> R.string.goal_lose
        Goal.MAINTAIN -> R.string.goal_maintain
        Goal.GAIN -> R.string.goal_gain
    }

    Card(
        modifier = modifier,
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = MaterialTheme.spacing.lg, horizontal = MaterialTheme.spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.profile_label_goal),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(MaterialTheme.sizing.legendDotSize)
                        .border(MaterialTheme.borderWidths.borderThick, MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(MaterialTheme.spacing.xxs)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.xs))
                Text(
                    text = stringResource(goalLabelRes),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
