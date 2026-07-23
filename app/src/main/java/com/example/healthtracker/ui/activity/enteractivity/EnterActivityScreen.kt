package com.example.healthtracker.ui.activity.enteractivity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.ui.activity.enteractivity.components.LabeledOutlinedField
import com.example.healthtracker.ui.component.entryform.EntryFormBanner
import com.example.healthtracker.ui.component.entryform.EntryFormInfoRow
import com.example.healthtracker.ui.component.entryform.EntryFormSaveBar
import com.example.healthtracker.ui.component.entryform.EntryFormTopBar
import com.example.healthtracker.ui.component.overlay.ConfirmDeleteDialog
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
            EntryFormTopBar(
                title = title,
                onClose = onClose,
                onDeleteClick = if (onDelete != null) { { showDeleteDialog = true } } else null,
                deleteEnabled = !uiState.isSaving && !uiState.isDeleting,
            )
        },
        bottomBar = {
            EntryFormSaveBar(
                onSave = onSave,
                enabled = !uiState.isSaving && !uiState.isDeleting,
                isSaving = uiState.isSaving,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            EntryFormBanner(
                icon = Icons.Default.DirectionsRun,
                text = stringResource(R.string.text_custom_activity_banner),
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledOutlinedField(
                label = stringResource(R.string.field_activity_name),
                value = uiState.name,
                onValueChange = onNameChange,
                placeholder = stringResource(R.string.field_activity_name_placeholder),
                error = uiState.nameError,
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledOutlinedField(
                label = stringResource(R.string.field_met_required),
                value = uiState.metInput,
                onValueChange = onMetChange,
                placeholder = "0",
                error = uiState.metError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            Spacer(modifier = Modifier.height(24.dp))

            EntryFormInfoRow(text = stringResource(R.string.text_custom_activity_info))
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
