package com.example.healthtracker.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DailyStatsRow(
    goal: Int,
    eaten: Int,
    burned: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            label = "GOAL",
            value = "$goal",
            valueColor = Color(0xFF1C2B33)
        )

        VerticalDivider(
            modifier = Modifier.height(32.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )

        StatItem(
            label = "EATEN",
            value = "$eaten",
            valueColor = Color(0xFFFF7043) // Orange/coral
        )

        VerticalDivider(
            modifier = Modifier.height(32.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )

        StatItem(
            label = "BURNED",
            value = "$burned",
            valueColor = Color(0xFF1B5E20) // Dark green
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    valueColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}
