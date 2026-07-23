package com.example.healthtracker.ui.activity.activitydiary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthtracker.R
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.spacing

@Composable
fun TotalBurnedTodayCard(
    totalCalories: Double,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.appShapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = MaterialTheme.spacing.xxl),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.total_burned_today_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                letterSpacing = 1.sp,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))
            Row {
                Text(
                    text = "${totalCalories.toInt()}",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.alignByBaseline(),
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.sm))
                Text(
                    text = stringResource(R.string.unit_kcal),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.alignByBaseline(),
                )
            }
        }
    }
}
