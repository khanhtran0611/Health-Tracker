package com.example.healthtracker.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthtracker.ui.theme.HealthTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .verticalScroll(rememberScrollState())
    ) {
        // TopBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.DarkGray)
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20), // Dark green
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Divider(color = Color.LightGray.copy(alpha = 0.3f))

        Spacer(modifier = Modifier.height(16.dp))

        // Language Section
        SectionTitle(title = "Language")
        SettingsCard {
            Text(
                text = "App language",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1C2B33),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomSegmentedControl(
                options = listOf("English", "Tiếng Việt"),
                selectedIndex = 0,
                onOptionSelected = {}
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Appearance Section
        SectionTitle(title = "Appearance")
        SettingsCard {
            Text(
                text = "Brightness",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1C2B33),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomSegmentedControl(
                options = listOf("Light", "Dark", "System"),
                selectedIndex = 0,
                onOptionSelected = {}
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.3f))

            Text(
                text = "Color theme",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1C2B33),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            ColorThemeSelector(
                colors = listOf(
                    Color(0xFF1B5E20), // Green
                    Color(0xFF0277BD), // Blue
                    Color(0xFFA14828), // Brown/Orange
                    Color(0xFF673AB7), // Purple
                    Color(0xFFC62828)  // Red
                ),
                selectedIndex = 0,
                onColorSelected = {}
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.3f))

            Text(
                text = "Font size",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1C2B33),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomSegmentedControl(
                options = listOf("Small", "Medium", "Large"),
                selectedIndex = 0,
                onOptionSelected = {}
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Reminders Section
        SectionTitle(title = "Reminders")
        SettingsCard(padding = 0.dp) {
            SwitchRow(
                title = "Daily logging reminders",
                checked = true,
                onCheckedChange = {},
                isBold = true
            )
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            SwitchRow(
                title = "Morning • 7:00 AM",
                checked = true,
                onCheckedChange = {},
                isBold = false
            )
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            SwitchRow(
                title = "Midday • 12:00 PM",
                checked = false,
                onCheckedChange = {},
                isBold = false
            )
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            SwitchRow(
                title = "Evening • 7:00 PM",
                checked = true,
                onCheckedChange = {},
                isBold = false
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Footer
        Text(
            text = "HealthTracker v1.0",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2E7D32), // Light green for headers
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsCard(
    padding: androidx.compose.ui.unit.Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(padding),
            content = content
        )
    }
}

@Composable
private fun CustomSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF2F2F2))
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, text ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .clickable { onOptionSelected(index) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFF1C2B33) else Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorThemeSelector(
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

@Composable
private fun SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isBold: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = Color(0xFF1C2B33)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1B5E20), // Dark green
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    HealthTrackerTheme {
        SettingScreen()
    }
}
