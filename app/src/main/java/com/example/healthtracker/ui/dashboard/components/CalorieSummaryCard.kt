package com.example.healthtracker.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.elevation
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun CalorieSummaryCard(
    eatenToday: Int,
    burnedToday: Int,
    balance: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.xl),
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.elevation.flat)
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.lg)
        ) {
            SummaryItem(
                icon = Icons.Default.RestaurantMenu,
                iconBgColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                label = stringResource(R.string.label_calories_eaten_today),
                value = "$eatenToday"
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.md, horizontal = MaterialTheme.spacing.xxxl),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            SummaryItem(
                icon = Icons.Default.DirectionsRun,
                iconBgColor = MaterialTheme.colorScheme.primaryContainer,
                iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                label = stringResource(R.string.label_calories_burned_today),
                value = "$burnedToday"
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.lgPlus))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.label_calorie_balance),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,

                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.xs))

                Text(
                    text = "$balance",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.sizing.iconContainerSize)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(MaterialTheme.sizing.iconMedium)
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.lg))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
