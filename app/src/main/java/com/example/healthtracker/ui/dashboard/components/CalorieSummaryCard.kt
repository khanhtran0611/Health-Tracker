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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat look from image, maybe slight shadow
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryItem(
                icon = Icons.Default.RestaurantMenu,
                iconBgColor = Color(0xFFFBE9E7),
                iconColor = Color(0xFFFF7043),
                label = "Calories eaten today",
                value = "$eatenToday"
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 48.dp),
                color = Color.LightGray.copy(alpha = 0.3f)
            )

            SummaryItem(
                icon = Icons.Default.DirectionsRun,
                iconBgColor = Color(0xFFE8F5E9),
                iconColor = Color(0xFF2E7D32),
                label = "Calories burned today",
                value = "$burnedToday"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Calorie balance: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Text(
                    text = "$balance",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C2B33)
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
                .size(36.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF1C2B33),
            modifier = Modifier.weight(1f)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1C2B33)
        )
    }
}
