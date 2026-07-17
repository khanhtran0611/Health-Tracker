package com.example.healthtracker.ui.mealdiary.addmealentry

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
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.ui.component.formatFoodCalorieInfo

/**
 * Thẻ hiện món đã chọn: chỉ tên + "{kcal} kcal / {servingUnit}" — KHÔNG có nút
 * đổi món (đổi món thì back về Food Picker để chọn lại, không sửa ở đây).
 */
@Composable
fun FoodSummaryCard(
    food: Food,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(food.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = formatFoodCalorieInfo(food),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
