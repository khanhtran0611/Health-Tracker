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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Chỉ hiện ĐÚNG 1 trong 2 hint, tuỳ tình trạng calo hiện tại (đúng mục "Lời
 * khuyên ngắn dựa trên tình trạng hiện tại" trong docs/requirement.md 2.5):
 * còn dư calo (remaining > 0) -> gợi ý thêm bữa ăn; đã đủ/vượt quá -> gợi ý
 * thêm hoạt động để đốt bớt.
 */
@Composable
fun DashboardActionButtons(
    showAddMealHint: Boolean,
    onAddMealClick: () -> Unit,
    onAddActivityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showAddMealHint) {
            CustomActionButton(
                icon = Icons.Default.RestaurantMenu,
                text = "Add meal",
                onClick = onAddMealClick
            )
        } else {
            CustomActionButton(
                icon = Icons.Default.DirectionsRun,
                text = "Add activity",
                onClick = onAddActivityClick
            )
        }
    }
}

@Composable
private fun CustomActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)), // Dark green
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
                        tint = Color.Yellow, 
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Hint:", 
                        color = Color.White.copy(alpha = 0.9f), 
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Main Row: Icon + Text
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = text, 
                        color = Color.White, 
                        fontWeight = FontWeight.SemiBold, 
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            // Bottom Right: Arrow Right
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
