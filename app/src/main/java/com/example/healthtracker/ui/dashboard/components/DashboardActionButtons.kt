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

/**
 * Chỉ hiện ĐÚNG 1 trong 2 hint, tuỳ tình trạng calo hiện tại (đúng mục "Lời
 * khuyên ngắn dựa trên tình trạng hiện tại" trong docs/requirement.md 2.5):
 * còn dư calo -> gợi ý thêm bữa ăn; đã vượt -> gợi ý thêm hoạt động để đốt
 * bớt; vừa đủ -> KHÔNG hiện nút nào cả.
 */
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
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                // Top Left: Lightbulb + "Hint: "
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        stringResource(R.string.label_hint_prefix),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Main Row: Icon + Text
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Bottom Right: Arrow Right
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
