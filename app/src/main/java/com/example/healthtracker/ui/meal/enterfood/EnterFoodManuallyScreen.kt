package com.example.healthtracker.ui.meal.enterfood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.ui.component.entryform.EntryFormBanner
import com.example.healthtracker.ui.component.entryform.EntryFormInfoRow
import com.example.healthtracker.ui.component.entryform.EntryFormSaveBar
import com.example.healthtracker.ui.component.entryform.EntryFormTopBar
import com.example.healthtracker.ui.component.overlay.ConfirmDeleteDialog
import com.example.healthtracker.ui.meal.enterfood.components.LabeledOutlinedField
import com.example.healthtracker.ui.theme.HealthTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterFoodManuallyScreen(
    food: Food?,
    onClose: () -> Unit,
    viewModel: EnterFoodManuallyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(food) {
        viewModel.initialize(food)
    }

    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onClose() }
    }

    EnterFoodManuallyContent(
        title = if (food == null) {
            stringResource(R.string.enter_food_manually_add_title)
        } else {
            stringResource(R.string.enter_food_manually_edit_title)
        },
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onCaloriesChange = viewModel::onCaloriesChange,
        onServingUnitChange = viewModel::onServingUnitChange,
        onSave = viewModel::onSubmit,
        onClose = onClose,

        onDelete = if (food != null) viewModel::onDelete else null,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterFoodManuallyContent(
    title: String,
    uiState: EnterFoodManuallyUiState,
    onNameChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit,
    onServingUnitChange: (String) -> Unit,
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
                icon = Icons.Default.Restaurant,
                text = stringResource(R.string.text_custom_food_banner),
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledOutlinedField(
                label = stringResource(R.string.field_food_name),
                value = uiState.name,
                onValueChange = onNameChange,
                placeholder = stringResource(R.string.field_food_name_placeholder),
                error = uiState.nameError,
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledOutlinedField(
                label = stringResource(R.string.field_calories_required),
                value = uiState.caloriesInput,
                onValueChange = onCaloriesChange,
                placeholder = "0",
                error = uiState.caloriesError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    Text(
                        text = stringResource(R.string.unit_kcal),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 16.dp),
                    )
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledOutlinedField(
                label = stringResource(R.string.field_serving_unit_optional),
                value = uiState.servingUnit,
                onValueChange = onServingUnitChange,
                placeholder = stringResource(R.string.field_serving_unit_placeholder),
            )

            Spacer(modifier = Modifier.height(24.dp))

            EntryFormInfoRow(text = stringResource(R.string.text_custom_food_info))
        }
    }

    if (showDeleteDialog && onDelete != null) {
        ConfirmDeleteDialog(
            title = stringResource(R.string.dialog_delete_food_title),
            message = stringResource(R.string.dialog_delete_food_message, uiState.name),
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
private fun EnterFoodManuallyContentPreview() {
    HealthTrackerTheme {
        EnterFoodManuallyContent(
            title = stringResource(R.string.enter_food_manually_add_title),
            uiState = EnterFoodManuallyUiState(),
            onNameChange = {},
            onCaloriesChange = {},
            onServingUnitChange = {},
            onSave = {},
            onClose = {},
        )
    }
}
