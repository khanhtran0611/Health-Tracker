package com.example.healthtracker.ui.activity.addactivityentry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.ui.activity.addactivityentry.components.ActivitySummaryCard
import com.example.healthtracker.ui.activity.addactivityentry.components.CaloriesBurnedCard
import com.example.healthtracker.ui.activity.addactivityentry.components.DurationStepper
import com.example.healthtracker.ui.component.formatting.formatDiaryDate
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

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

    val localContext = LocalContext.current
    val localConfiguration = LocalConfiguration.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        CompositionLocalProvider(
            LocalContext provides localContext,
            LocalConfiguration provides localConfiguration,
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
}

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
            .padding(horizontal = MaterialTheme.spacing.lg, vertical = MaterialTheme.spacing.sm),
    ) {

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
                        modifier = Modifier.size(MaterialTheme.sizing.iconSmall),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        strokeWidth = MaterialTheme.borderWidths.borderThick,
                    )
                    Spacer(Modifier.padding(end = MaterialTheme.spacing.sm))
                }
                Text(stringResource(R.string.action_save))
            }
        }

        Spacer(Modifier.height(MaterialTheme.spacing.lg))

        val activity = uiState.activity
        if (activity == null) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.xxl),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {

            ActivitySummaryCard(activity = activity)

            Spacer(Modifier.height(MaterialTheme.spacing.xxl))
            Text(
                stringResource(R.string.label_duration),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(MaterialTheme.spacing.lg))
            DurationStepper(
                durationMinutes = uiState.durationMinutes,
                onDecrease = onDecreaseDuration,
                onIncrease = onIncreaseDuration,
            )

            Spacer(Modifier.height(MaterialTheme.spacing.xxl))
            CaloriesBurnedCard(caloriesBurned = uiState.caloriesBurned)

            Spacer(Modifier.height(MaterialTheme.spacing.lg))
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

        Spacer(Modifier.height(MaterialTheme.spacing.lg))
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
