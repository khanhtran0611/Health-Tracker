package com.example.healthtracker.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.dashboard.CalorieStatus
import kotlin.math.abs

/**
 * Lời khuyên ngắn dựa trên tình trạng calo hiện tại — đúng docs/requirement.md
 * 2.5: còn thiếu -> "Bạn còn thiếu X kcal hôm nay"; vừa đủ -> "Bạn đã ăn đủ
 * calo hôm nay!"; vượt quá -> "Bạn đã vượt X kcal hôm nay".
 */
@Composable
fun CalorieNeedIndicator(
    remainingCalories: Int,
    calorieStatus: CalorieStatus,
    modifier: Modifier = Modifier
) {
    val contentColor = if (calorieStatus == CalorieStatus.OVER_TARGET) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }
    val text = when (calorieStatus) {
        CalorieStatus.UNDER_TARGET -> stringResource(R.string.dashboard_need_under_target, remainingCalories)
        CalorieStatus.ON_TARGET -> stringResource(R.string.dashboard_need_on_target)
        CalorieStatus.OVER_TARGET -> stringResource(R.string.dashboard_need_over_target, abs(remainingCalories))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(contentColor.copy(alpha = 0.15f))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor
        )
    }
}
