package com.example.healthtracker.ui.activitydiary.addactivityentry

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R

@Composable
fun DurationStepper(
    durationMinutes: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        IconButton(
            onClick = onDecrease,
            modifier = Modifier
                .size(48.dp)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
        ) {
            Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.action_decrease_duration))
        }

        Spacer(Modifier.width(32.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$durationMinutes",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.unit_minutes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.width(32.dp))

        IconButton(
            onClick = onIncrease,
            modifier = Modifier
                .size(48.dp)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.action_increase_duration))
        }
    }
}
