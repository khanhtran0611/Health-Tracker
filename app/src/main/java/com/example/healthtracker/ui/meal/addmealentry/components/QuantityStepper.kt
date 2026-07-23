package com.example.healthtracker.ui.meal.addmealentry.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R

@Composable
fun QuantityStepper(
    quantity: Double,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), androidx.compose.foundation.shape.RoundedCornerShape(999.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StepperButton(
                icon = Icons.Default.Remove,
                contentDescription = stringResource(R.string.action_decrease_quantity),
                onClick = onDecrease,
            )
            Text(
                text = formatQuantity(quantity),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            StepperButton(
                icon = Icons.Default.Add,
                contentDescription = stringResource(R.string.action_increase_quantity),
                onClick = onIncrease,
            )
        }
        Text(
            text = stringResource(R.string.unit_serving_short),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Composable
private fun StepperButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp),
        )
    }
}

private fun formatQuantity(quantity: Double): String {
    return if (quantity == quantity.toInt().toDouble()) {
        quantity.toInt().toString()
    } else {
        quantity.toString()
    }
}
