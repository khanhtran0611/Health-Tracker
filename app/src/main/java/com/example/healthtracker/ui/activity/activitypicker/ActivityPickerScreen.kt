package com.example.healthtracker.ui.activity.activitypicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.ui.activity.activitypicker.components.ActivityListItem
import com.example.healthtracker.ui.activity.activitypicker.components.ManualActivityEntryCard
import com.example.healthtracker.ui.activity.addactivityentry.AddActivityEntryScreen
import com.example.healthtracker.ui.component.overlay.LoadingOverlay
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate
import com.example.healthtracker.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityPickerScreen(
    logDate: LocalDate,
    onBack: () -> Unit,
    onEnterNewActivity: () -> Unit,
    onEditActivity: (Activity) -> Unit,
    viewModel: ActivityPickerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedActivityForEntry by remember { mutableStateOf<Activity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.activity_picker_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        ActivityPickerContent(
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onActivitySelected = { selectedActivityForEntry = it },
            onEnterNewActivity = onEnterNewActivity,
            onEditActivity = onEditActivity,
            modifier = Modifier.padding(padding),
        )
    }

    if (selectedActivityForEntry != null) {
        AddActivityEntryScreen(
            activity = selectedActivityForEntry!!,
            logDate = logDate,
            onDismiss = { selectedActivityForEntry = null },
        )
    }
}

@Composable
fun ActivityPickerContent(
    uiState: ActivityPickerUiState,
    onSearchQueryChange: (String) -> Unit,
    onActivitySelected: (Activity) -> Unit,
    onEnterNewActivity: () -> Unit,
    onEditActivity: (Activity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.lg),
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text(stringResource(R.string.activity_picker_search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = MaterialTheme.spacing.sm),
            ) {
                items(uiState.activities, key = { it.id }) { activity ->
                    ActivityListItem(
                        activity = activity,
                        onClick = { onActivitySelected(activity) },
                        onEditClick = { onEditActivity(activity) },
                    )
                }
                item {
                    ManualActivityEntryCard(
                        onClick = onEnterNewActivity,
                        modifier = Modifier.padding(top = MaterialTheme.spacing.lg),
                    )
                }
            }
        }

        if (uiState.isLoading) {
            LoadingOverlay(textRes = R.string.text_loading)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActivityPickerContentPreview() {
    HealthTrackerTheme {
        ActivityPickerContent(
            uiState = ActivityPickerUiState(
                searchQuery = "",
                isLoading = false,
                activities = listOf(
                    Activity(id = 1, name = "Đi bộ", met = 3.5),
                    Activity(id = 2, name = "Chạy bộ", met = 9.8),
                    Activity(id = 3, name = "Đạp xe", met = 7.5),
                    Activity(id = 4, name = "Bơi lội", met = 8.0),
                    Activity(id = 5, name = "Yoga", met = 2.5),
                ),
            ),
            onSearchQueryChange = {},
            onActivitySelected = {},
            onEditActivity = {},
            onEnterNewActivity = {},
        )
    }
}
