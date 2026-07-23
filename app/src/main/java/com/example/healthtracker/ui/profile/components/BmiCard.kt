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
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

private const val BMI_SCALE_MAX = 40.0

@Composable
fun BmiCard(bmi: Double, category: BmiCategory, modifier: Modifier = Modifier) {
    val categoryColor = bmiCategoryColor(category)

    val roundedBmi = (bmi * 10).roundToInt() / 10.0

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.appShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.lg)) {
            Text(
                text = stringResource(R.string.profile_bmi_title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xs))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = roundedBmi.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.appShapes.medium)
                        .background(categoryColor)
                        .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.xs),
                ) {
                    Text(
                        text = stringResource(bmiCategoryLabelRes(category)),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

            BmiBar(bmi = bmi)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("18.5", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("25", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("30", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun BmiBar(bmi: Double, modifier: Modifier = Modifier) {
    val thumbSize = MaterialTheme.sizing.bmiThumbSize
    val fraction = (bmi / BMI_SCALE_MAX).coerceIn(0.0, 1.0).toFloat()

    BoxWithConstraints(modifier = modifier.fillMaxWidth().height(thumbSize), contentAlignment = Alignment.CenterStart) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.sizing.progressBarHeight)
                .clip(MaterialTheme.appShapes.full),
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
                .border(MaterialTheme.borderWidths.borderThick, MaterialTheme.colorScheme.onSurface, CircleShape)
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
