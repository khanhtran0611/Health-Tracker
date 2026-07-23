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
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun QuantityStepper(
    quantity: Double,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.border(BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant), MaterialTheme.appShapes.full),
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
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.lg),
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
            modifier = Modifier.padding(start = MaterialTheme.spacing.md),
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
            .size(MaterialTheme.sizing.quantityStepperButtonSize)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(MaterialTheme.sizing.stepperIconSize),
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
