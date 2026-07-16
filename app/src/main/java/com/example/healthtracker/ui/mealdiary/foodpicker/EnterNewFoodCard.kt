package com.example.healthtracker.ui.mealdiary.foodpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R

/** Thẻ viền đứt nét ở cuối danh sách: gợi ý tự nhập món khi không tìm thấy trong catalog. */
@Composable
fun EnterNewFoodCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = outlineColor,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)),
                    ),
                    cornerRadius = CornerRadius(16.dp.toPx()),
                )
            }
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                .padding(12.dp),
        )
        Text(
            stringResource(R.string.food_picker_empty_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            stringResource(R.string.food_picker_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            shape = RoundedCornerShape(999.dp),
        ) {
            Text(stringResource(R.string.action_enter_new_food))
        }
    }
}
