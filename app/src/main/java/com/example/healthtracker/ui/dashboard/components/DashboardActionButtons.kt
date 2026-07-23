package com.example.healthtracker.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.dashboard.CalorieStatus
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun DashboardActionButtons(
    calorieStatus: CalorieStatus,
    onAddMealClick: () -> Unit,
    onAddActivityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (calorieStatus == CalorieStatus.ON_TARGET) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg)
    ) {
        if (calorieStatus == CalorieStatus.UNDER_TARGET) {
            CustomActionButton(
                icon = Icons.Default.RestaurantMenu,
                text = stringResource(R.string.action_add_meal),
                onClick = onAddMealClick
            )
        } else {
            CustomActionButton(
                icon = Icons.Default.DirectionsRun,
                text = stringResource(R.string.action_add_activity),
                onClick = onAddActivityClick
            )
        }
    }
}

@Composable
private fun CustomActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.appShapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.lg)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(MaterialTheme.sizing.iconMedium)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.xsPlus))
                    Text(
                        stringResource(R.string.label_hint_prefix),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.sm))
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
