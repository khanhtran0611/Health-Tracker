package com.example.healthtracker.ui.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun TdeeCard(tdee: Int, activityLevelTitleRes: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.lg)) {
            Text(
                text = stringResource(R.string.profile_tdee_title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))

            Row {
                Text(
                    text = "$tdee",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.alignByBaseline(),
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.sm))
                Text(
                    text = stringResource(R.string.unit_kcal),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.alignByBaseline(),
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DirectionsRun,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(MaterialTheme.sizing.iconSmall),
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.xs))
                Text(
                    text = stringResource(R.string.profile_tdee_based_on, stringResource(activityLevelTitleRes)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
