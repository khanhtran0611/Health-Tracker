package com.example.healthtracker.ui.activitydiary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.ActivityEntry
import com.example.healthtracker.ui.component.DateNavigator
import com.example.healthtracker.ui.component.LoadingOverlay
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate

/** Điểm vào thật — nối ViewModel qua Hilt. Phần hiển thị thật nằm ở [ActivityDiaryContent]. */
@Composable
fun ActivityDiaryScreen(
    onAddActivity: (LocalDate) -> Unit,
    viewModel: ActivityDiaryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ActivityDiaryContent(
        uiState = uiState,
        onPreviousDay = viewModel::onPreviousDay,
        onNextDay = viewModel::onNextDay,
        onDateSelected = viewModel::onDateSelected,
        onAddActivity = { onAddActivity(uiState.selectedDate) },
        onDeleteEntry = viewModel::onDeleteEntry,
    )
}

/**
 * Phần hiển thị THUẦN, không đụng ViewModel/Hilt — tách riêng khỏi [ActivityDiaryScreen]
 * để @Preview dùng được.
 */
@Composable
fun ActivityDiaryContent(
    uiState: ActivityDiaryUiState,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onAddActivity: () -> Unit,
    onDeleteEntry: (ActivityEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { /* TODO: mở drawer khi có logic */ },
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(Icons.Default.Menu, contentDescription = null)
                }
                Text(
                    text = stringResource(R.string.activity_diary_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            DateNavigator(
                selectedDate = uiState.selectedDate,
                onPreviousDay = onPreviousDay,
                onNextDay = onNextDay,
                onDateSelected = onDateSelected,
            )

            TotalBurnedTodayCard(totalCalories = uiState.totalCaloriesBurnedToday)

            if (uiState.entries.isEmpty()) {
                Text(
                    text = stringResource(R.string.activity_diary_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    textAlign = TextAlign.Center,
                )
            } else {
                uiState.entries.forEach { entry ->
                    ActivityEntryCard(entry = entry, onDelete = { onDeleteEntry(entry) })
                }
            }

            Button(
                onClick = onAddActivity,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(28.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.action_add_activity),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        if (uiState.isLoading) {
            LoadingOverlay(textRes = R.string.text_loading)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActivityDiaryContentPreview() {
    HealthTrackerTheme {
        ActivityDiaryContent(
            uiState = ActivityDiaryUiState(
                selectedDate = LocalDate.now(),
                entries = listOf(
                    ActivityEntry(
                        id = 1,
                        activityId = 1,
                        logDate = LocalDate.now(),
                        activityName = "Chạy bộ",
                        durationMinutes = 30,
                        caloriesBurned = 294.0,
                    ),
                    ActivityEntry(
                        id = 2,
                        activityId = 2,
                        logDate = LocalDate.now(),
                        activityName = "Đạp xe",
                        durationMinutes = 45,
                        caloriesBurned = 410.0,
                    ),
                ),
                totalCaloriesBurnedToday = 704.0,
            ),
            onPreviousDay = {},
            onNextDay = {},
            onDateSelected = {},
            onAddActivity = {},
            onDeleteEntry = {},
        )
    }
}
