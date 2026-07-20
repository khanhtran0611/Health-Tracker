package com.example.healthtracker.ui.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.profile.BmiCategory
import kotlin.math.roundToInt

/** Thang hiển thị của thanh BMI — bao trọn cả 4 mức (Thiếu cân..Béo phì), khớp mốc 18.5/25/30 theo đề. */
private const val BMI_SCALE_MAX = 40.0

@Composable
fun BmiCard(bmi: Double, category: BmiCategory, modifier: Modifier = Modifier) {
    val categoryColor = bmiCategoryColor(category)
    // Làm tròn 1 chữ số thập phân bằng số học thay vì String.format — tránh dấu
    // thập phân đổi theo locale máy (vd "22,9" ở vi-VN thay vì "22.9").
    val roundedBmi = (bmi * 10).roundToInt() / 10.0

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.profile_bmi_title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = roundedBmi.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(categoryColor)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = stringResource(bmiCategoryLabelRes(category)),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BmiBar(bmi = bmi)

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("18.5", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("25", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("30", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** Thanh 4 đoạn (Thiếu cân/Bình thường/Thừa cân/Béo phì) + chấm tròn đánh dấu vị trí BMI hiện tại. */
@Composable
private fun BmiBar(bmi: Double, modifier: Modifier = Modifier) {
    val thumbSize = 16.dp
    val fraction = (bmi / BMI_SCALE_MAX).coerceIn(0.0, 1.0).toFloat()

    BoxWithConstraints(modifier = modifier.fillMaxWidth().height(thumbSize), contentAlignment = Alignment.CenterStart) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
        ) {
            Box(modifier = Modifier.weight(18.5f).fillMaxHeight().background(bmiCategoryColor(BmiCategory.UNDERWEIGHT)))
            Box(modifier = Modifier.weight(6.5f).fillMaxHeight().background(bmiCategoryColor(BmiCategory.NORMAL)))
            Box(modifier = Modifier.weight(5f).fillMaxHeight().background(bmiCategoryColor(BmiCategory.OVERWEIGHT)))
            Box(modifier = Modifier.weight(10f).fillMaxHeight().background(bmiCategoryColor(BmiCategory.OBESE)))
        }

        val thumbOffsetPx = (maxWidth - thumbSize) * fraction
        Box(
            modifier = Modifier
                .padding(start = thumbOffsetPx)
                .size(thumbSize)
                .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest, CircleShape),
        )
    }
}

@Composable
private fun bmiCategoryColor(category: BmiCategory): Color = when (category) {
    BmiCategory.UNDERWEIGHT -> MaterialTheme.colorScheme.tertiary
    BmiCategory.NORMAL -> MaterialTheme.colorScheme.primary
    BmiCategory.OVERWEIGHT -> MaterialTheme.colorScheme.secondary
    BmiCategory.OBESE -> MaterialTheme.colorScheme.error
}

private fun bmiCategoryLabelRes(category: BmiCategory): Int = when (category) {
    BmiCategory.UNDERWEIGHT -> R.string.bmi_category_underweight
    BmiCategory.NORMAL -> R.string.bmi_category_normal
    BmiCategory.OVERWEIGHT -> R.string.bmi_category_overweight
    BmiCategory.OBESE -> R.string.bmi_category_obese
}
