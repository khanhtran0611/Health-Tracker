package com.example.healthtracker.ui.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.elevation
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

data class ToastData(
    @param:StringRes val textRes: Int,
    val type: ToastType = ToastType.INFO
)

@Composable
fun SharedToast(
    toastData: ToastData?,
    modifier: Modifier = Modifier
) {

    var lastToastData by remember { mutableStateOf(toastData) }
    if (toastData != null) {
        lastToastData = toastData
    }
    val displayData = toastData ?: lastToastData

    AnimatedVisibility(
        visible = toastData != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        displayData?.let { data ->
            val backgroundColor = when (data.type) {
                ToastType.SUCCESS -> Color(0xFF4CAF50)
                ToastType.ERROR -> Color(0xFFF44336)
                ToastType.WARNING -> Color(0xFFFF9800)
                ToastType.INFO -> MaterialTheme.colorScheme.primary
            }

            val icon: ImageVector = when (data.type) {
                ToastType.SUCCESS -> Icons.Default.CheckCircle
                ToastType.ERROR -> Icons.Default.Error
                ToastType.WARNING -> Icons.Default.Warning
                ToastType.INFO -> Icons.Default.Info
            }

            Card(
                shape = MaterialTheme.appShapes.large,
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.elevation.toast),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.lg, vertical = MaterialTheme.spacing.sm)
            ) {
                Row(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.lg)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,

                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(MaterialTheme.sizing.iconLarge)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))
                    Text(

                        text = stringResource(data.textRes),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
