package com.example.healthtracker.ui.mealdiary.foodpicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.healthtracker.ui.component.formatFoodCalorieInfo

/** 1 dòng món ăn trong danh sách chọn: tên + khẩu phần/calo bên trái, nút "+" bên phải. */
@Composable
fun FoodListItem(
    food: Food,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
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
        // IconButton mặc định chiếm khối 48dp (vùng chạm a11y) dù icon chỉ 24dp -> ép
        // size(24.dp) lên chính IconButton để khối bằng đúng icon, áp sát mép phải
        // (giống fix đã làm ở MealEntryRow).
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(24.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.action_add_food))
        }
    }
}
