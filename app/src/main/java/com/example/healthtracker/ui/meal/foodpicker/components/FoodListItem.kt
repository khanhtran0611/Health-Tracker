package com.example.healthtracker.ui.meal.foodpicker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.ui.component.formatting.formatFoodCalorieInfo
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun FoodListItem(
    food: Food,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(food.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = formatFoodCalorieInfo(food),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(MaterialTheme.sizing.iconLarge),
            ) {
                Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.action_edit))
            }
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.lg)
                    .size(MaterialTheme.sizing.iconLarge),
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.action_add_food))
            }
        }
    }
}
