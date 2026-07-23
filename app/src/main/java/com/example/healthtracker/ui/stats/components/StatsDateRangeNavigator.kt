package com.example.healthtracker.ui.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.spacing

@Composable
fun StatsDateRangeNavigator(
    startDate: LocalDate,
    endDate: LocalDate,
    canGoToPreviousRange: Boolean,
    canGoToNextRange: Boolean,
    onPreviousRange: () -> Unit,
    onNextRange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM") }
    val rangeText = "${startDate.format(formatter)} – ${endDate.format(formatter)}"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.appShapes.full)
            .padding(vertical = MaterialTheme.spacing.xs, horizontal = MaterialTheme.spacing.xs),
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onPreviousRange, enabled = canGoToPreviousRange) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = stringResource(R.string.action_previous_range))
            }
            Text(rangeText, style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = onNextRange, enabled = canGoToNextRange) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = stringResource(R.string.action_next_range))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsDateRangeNavigatorPreview() {
    HealthTrackerTheme {
        StatsDateRangeNavigator(
            startDate = LocalDate.now().minusDays(6),
            endDate = LocalDate.now(),
            canGoToPreviousRange = true,
            canGoToNextRange = false,
            onPreviousRange = {},
            onNextRange = {},
        )
    }
}
