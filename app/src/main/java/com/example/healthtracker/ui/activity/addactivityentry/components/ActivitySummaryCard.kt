package com.example.healthtracker.ui.activity.addactivityentry.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.ui.component.formatting.formatActivityMetInfo
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.spacing

@Composable
fun ActivitySummaryCard(
    activity: Activity,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.lg)) {
            Text(activity.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = formatActivityMetInfo(activity),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
