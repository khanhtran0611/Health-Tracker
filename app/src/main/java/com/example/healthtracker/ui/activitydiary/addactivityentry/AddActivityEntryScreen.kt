package com.example.healthtracker.ui.activitydiary.addactivityentry

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.ui.component.formatActivityMetInfo
import com.example.healthtracker.ui.component.formatDiaryDate
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate

/**
 * Điểm vào thật — nối ViewModel qua Hilt, hiển thị dạng ModalBottomSheet. Vẫn nằm
 * trong backstack chung như mọi Route khác — [onDismiss] chỉ đơn giản gọi
 * backStack.removeLastOrNull(), không phát sinh cơ chế điều hướng riêng nào.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityEntryScreen(
    activity: Activity,
    logDate: LocalDate,
    onDismiss: () -> Unit,
    viewModel: AddActivityEntryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(activity, logDate) {
        viewModel.initialize(activity, logDate)
    }

    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onDismiss() }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        AddActivityEntryContent(
            uiState = uiState,
            onDecreaseDuration = viewModel::onDecreaseDuration,
            onIncreaseDuration = viewModel::onIncreaseDuration,
            onSave = viewModel::onSubmit,
            onClose = onDismiss,
        )
    }
}

/**
 * Phần hiển thị THUẦN, không đụng ViewModel/Hilt — tách riêng để @Preview dùng được.
 */
@Composable
fun AddActivityEntryContent(
    uiState: AddActivityEntryUiState,
    onDecreaseDuration: () -> Unit,
    onIncreaseDuration: () -> Unit,
    onSave: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        // Header "✕  Add activity  Save" — Box+Alignment giống AddMealEntryContent.
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.action_close))
            }
            Text(
                stringResource(R.string.add_activity_entry_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
            TextButton(
                onClick = onSave,
                enabled = uiState.activity != null && !uiState.isSaving,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                ),
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.padding(end = 8.dp))
                }
                Text(stringResource(R.string.action_save))
            }
        }

        Spacer(Modifier.height(16.dp))

        val activity = uiState.activity
        if (activity == null) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Thẻ hoạt động đã chọn: chỉ tên + MET — KHÔNG có nút đổi hoạt động
            // (đổi thì back về Choose Activity, giống FoodSummaryCard bên Meal Diary).
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(activity.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = formatActivityMetInfo(activity),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
            Text(
                stringResource(R.string.label_duration),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(16.dp))
            DurationStepper(
                durationMinutes = uiState.durationMinutes,
                onDecrease = onDecreaseDuration,
                onIncrease = onIncreaseDuration,
            )

            Spacer(Modifier.height(32.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            stringResource(R.string.label_calories_burned),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${uiState.caloriesBurned.toInt()}",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        Text(
                            " ${stringResource(R.string.unit_kcal)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    stringResource(R.string.label_logging_for),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(formatDiaryDate(uiState.logDate), style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun AddActivityEntryContentPreview() {
    HealthTrackerTheme {
        AddActivityEntryContent(
            uiState = AddActivityEntryUiState(
                activity = Activity(id = 1, name = "Chạy bộ", met = 9.8),
                durationMinutes = 30,
                logDate = LocalDate.now(),
                weightKg = 60.0,
            ),
            onDecreaseDuration = {},
            onIncreaseDuration = {},
            onSave = {},
            onClose = {},
        )
    }
}
