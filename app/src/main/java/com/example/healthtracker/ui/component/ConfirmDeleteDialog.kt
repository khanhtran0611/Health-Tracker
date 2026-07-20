package com.example.healthtracker.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R

/**
 * Dialog xác nhận trước khi xoá — dùng chung cho mọi hành động xoá không thể
 * hoàn tác (Enter Food Manually, Enter Activity Manually, ...). Vẫn là
 * `AlertDialog` chuẩn của Material3, chỉ tuỳ chỉnh icon/bo góc/màu nút xác
 * nhận để nhìn rõ đây là hành động phá huỷ (destructive).
 */
@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    // AlertDialog dựng Dialog riêng, bên trong tự lấy lại LocalContext/
    // LocalConfiguration từ Window thật (Activity gốc) chứ không kế thừa bản đã
    // đổi ngôn ngữ của LocalizedApp -> bắt lại 2 Local này ở NGOÀI (đúng ngôn ngữ)
    // rồi re-provide vào các slot bên trong dùng stringResource().
    val localContext = LocalContext.current
    val localConfiguration = LocalConfiguration.current

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.DeleteForever,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = { Text(title) },
        text = { Text(message) },
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        confirmButton = {
            CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Text(stringResource(R.string.action_delete))
                }
            }
        },
        dismissButton = {
            CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        },
    )
}
