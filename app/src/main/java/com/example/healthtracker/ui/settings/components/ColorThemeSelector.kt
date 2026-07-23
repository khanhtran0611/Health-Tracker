package com.example.healthtracker.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun ColorThemeSelector(
    colors: List<Color>,
    selectedIndex: Int,
    onColorSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg)
    ) {
        colors.forEachIndexed { index, color ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .size(MaterialTheme.sizing.colorSwatchSize)
                    .border(
                        width = if (isSelected) MaterialTheme.borderWidths.borderThick else MaterialTheme.borderWidths.borderNone,
                        color = if (isSelected) color else Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(MaterialTheme.spacing.xs)
                    .background(color, CircleShape)
                    .clickable { onColorSelected(index) }
            )
        }
    }
}
