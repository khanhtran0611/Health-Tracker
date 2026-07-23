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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorThemeSelector(
    colors: List<Color>,
    selectedIndex: Int,
    onColorSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        colors.forEachIndexed { index, color ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) color else Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .background(color, CircleShape)
                    .clickable { onColorSelected(index) }
            )
        }
    }
}
