package com.example.healthtracker.ui.activitydiary.enteractivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.ui.component.ConfirmDeleteDialog
import com.example.healthtracker.ui.component.fieldErrorText
import com.example.healthtracker.ui.theme.HealthTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterActivityScreen(
    activity: Activity?,
    onClose: () -> Unit,
    viewModel: EnterActivityViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(activity) {
        viewModel.initialize(activity)
    }

    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onClose() }
    }

    EnterActivityContent(
        title = if (activity == null) {
            stringResource(R.string.enter_activity_add_title)
        } else {
            stringResource(R.string.enter_activity_edit_title)
        },
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onMetChange = viewModel::onMetChange,
        onSave = viewModel::onSubmit,
        onClose = onClose,

        onDelete = if (activity != null) viewModel::onDelete else null,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterActivityContent(
    title: String,
    uiState: EnterActivityUiState,
    onNameChange: (String) -> Unit,
    onMetChange: (String) -> Unit,
    onSave: () -> Unit,
    onClose: () -> Unit,
    onDelete: (() -> Unit)? = null,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.action_close))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Center),
                    )
                    if (onDelete != null) {
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            enabled = !uiState.isSaving && !uiState.isDeleting,
                            modifier = Modifier.align(Alignment.CenterEnd),
                        ) {
                            Icon(
                                Icons.Default.DeleteOutline,
                                contentDescription = stringResource(R.string.action_delete),
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                Button(
                    onClick = onSave,
                    enabled = !uiState.isSaving && !uiState.isDeleting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(stringResource(R.string.action_save_and_use))
                    }
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.text_custom_activity_banner),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.field_activity_name),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                placeholder = { Text(stringResource(R.string.field_activity_name_placeholder)) },
                isError = uiState.nameError != null,
                supportingText = fieldErrorText(uiState.nameError)?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.field_met_required),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.metInput,
                onValueChange = onMetChange,
                placeholder = { Text("0") },
                isError = uiState.metError != null,
                supportingText = fieldErrorText(uiState.metError)?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.text_custom_activity_info),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    if (showDeleteDialog && onDelete != null) {
        ConfirmDeleteDialog(
            title = stringResource(R.string.dialog_delete_activity_title),
            message = stringResource(R.string.dialog_delete_activity_message, uiState.name),
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnterActivityContentPreview() {
    HealthTrackerTheme {
        EnterActivityContent(
            title = stringResource(R.string.enter_activity_add_title),
            uiState = EnterActivityUiState(),
            onNameChange = {},
            onMetChange = {},
            onSave = {},
            onClose = {},
        )
    }
}
