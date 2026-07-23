package com.example.healthtracker.ui.meal.mealdiary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Icecream
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.MealEntry
import com.example.healthtracker.domain.model.MealType

@Composable
fun MealTypeSection(
    mealType: MealType,
    entries: List<MealEntry>,
    totalCalories: Double,
    onAddFood: () -> Unit,
    onDeleteEntry: (MealEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        mealTypeIcon(mealType),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        stringResource(mealTypeLabelRes(mealType)),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
                Text(
                    text = "${totalCalories.toInt()} ${stringResource(R.string.unit_kcal)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (totalCalories > 0) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }

            if (entries.isEmpty()) {
                Text(
                    stringResource(R.string.section_no_items_yet),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .align(Alignment.CenterHorizontally),
                )
            } else {
                entries.forEach { entry ->
                    MealEntryRow(entry = entry, onDelete = { onDeleteEntry(entry) })
                }
            }

            TextButton(
                onClick = onAddFood,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text(
                    stringResource(R.string.action_add_food),
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}

private fun mealTypeIcon(mealType: MealType): ImageVector = when (mealType) {
    MealType.BREAKFAST -> Icons.Default.BreakfastDining
    MealType.LUNCH -> Icons.Default.LunchDining
    MealType.DINNER -> Icons.Default.DinnerDining
    MealType.SNACK -> Icons.Default.Icecream
}

private fun mealTypeLabelRes(mealType: MealType): Int = when (mealType) {
    MealType.BREAKFAST -> R.string.meal_type_breakfast
    MealType.LUNCH -> R.string.meal_type_lunch
    MealType.DINNER -> R.string.meal_type_dinner
    MealType.SNACK -> R.string.meal_type_snack
}
