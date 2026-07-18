package com.example.healthtracker.ui.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.unit.dp

data class ToastData(
    val message: String,
    val type: ToastType = ToastType.INFO
)

@Composable
fun SharedToast(
    toastData: ToastData?,
    modifier: Modifier = Modifier
) {
    // Lưu lại data cũ để Toast không bị xoá ruột trước khi chạy xong hiệu ứng trượt lên (exit)
    // Cập nhật ĐỒNG BỘ ngay trong composition (không dùng LaunchedEffect) 
    // để tránh bị delay 1 frame làm mất hiệu ứng trượt xuống (enter)
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
                ToastType.SUCCESS -> Color(0xFF4CAF50) // Green
                ToastType.ERROR -> Color(0xFFF44336)   // Red
                ToastType.WARNING -> Color(0xFFFF9800) // Orange
                ToastType.INFO -> MaterialTheme.colorScheme.primary
            }

            val icon: ImageVector = when (data.type) {
                ToastType.SUCCESS -> Icons.Default.CheckCircle
                ToastType.ERROR -> Icons.Default.Error
                ToastType.WARNING -> Icons.Default.Warning
                ToastType.INFO -> Icons.Default.Info
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Toast Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = data.message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
