package com.example.healthtracker.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Form nhập hồ sơ dùng CHUNG cho Onboarding và Edit Profile — 2 màn chỉ khác nhau
 * ở topBar bên ngoài (có back hay không) và nhãn nút submit ("Next"/"Save").
 */
@Composable
fun ProfileForm(
    state: ProfileFormUiState,
    onFullNameChange: (String) -> Unit,
    onDateOfBirthChange: (LocalDate) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onActivityLevelChange: (Int) -> Unit,
    onGoalChange: (Goal) -> Unit,
    onSubmit: () -> Unit,
    submitLabel: String,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = state.fullName,
            onValueChange = onFullNameChange,
            label = { Text(stringResource(R.string.field_full_name)) },
            isError = state.fullNameError != null,
            supportingText = fieldErrorText(state.fullNameError)?.let { { Text(it) } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        DateOfBirthField(
            dateOfBirth = state.dateOfBirth,
            age = state.age,
            error = state.dateOfBirthError,
            onDateChange = onDateOfBirthChange,
        )

        GenderSelector(gender = state.gender, onGenderChange = onGenderChange)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = state.weightKg,
                onValueChange = onWeightChange,
                label = { Text(stringResource(R.string.field_weight)) },
                suffix = { Text(stringResource(R.string.unit_kg)) },
                isError = state.weightError != null,
                supportingText = fieldErrorText(state.weightError)?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = state.heightCm,
                onValueChange = onHeightChange,
                label = { Text(stringResource(R.string.field_height)) },
                suffix = { Text(stringResource(R.string.unit_cm)) },
                isError = state.heightError != null,
                supportingText = fieldErrorText(state.heightError)?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
        }

        Text(stringResource(R.string.section_activity_level), style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ActivityLevelOption.entries.forEach { option ->
                ActivityLevelCard(
                    option = option,
                    selected = state.activityLevel == option.level,
                    onClick = { onActivityLevelChange(option.level) },
                )
            }
        }

        Text(stringResource(R.string.section_goal), style = MaterialTheme.typography.titleMedium)
        GoalSelector(goal = state.goal, onGoalChange = onGoalChange)

        Button(
            onClick = onSubmit,
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(submitLabel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateOfBirthField(
    dateOfBirth: LocalDate?,
    age: Int?,
    error: FieldError?,
    onDateChange: (LocalDate) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    Column {
        OutlinedTextField(
            value = dateOfBirth?.format(formatter) ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.field_date_of_birth)) },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            isError = error != null,
            supportingText = fieldErrorText(error)?.let { { Text(it) } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        if (age != null) {
            Text(
                text = stringResource(R.string.field_age_value, age),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dateOfBirth
                ?.atStartOfDay(ZoneOffset.UTC)
                ?.toInstant()
                ?.toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        onDateChange(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.action_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderSelector(gender: Gender, onGenderChange: (Gender) -> Unit) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = gender == Gender.MALE,
            onClick = { onGenderChange(Gender.MALE) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
        ) {
            Text(stringResource(R.string.gender_male))
        }
        SegmentedButton(
            selected = gender == Gender.FEMALE,
            onClick = { onGenderChange(Gender.FEMALE) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
        ) {
            Text(stringResource(R.string.gender_female))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalSelector(goal: Goal, onGoalChange: (Goal) -> Unit) {
    val options = listOf(
        Goal.LOSE to R.string.goal_lose,
        Goal.MAINTAIN to R.string.goal_maintain,
        Goal.GAIN to R.string.goal_gain,
    )
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, (value, labelRes) ->
            SegmentedButton(
                selected = goal == value,
                onClick = { onGoalChange(value) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
            ) {
                Text(stringResource(labelRes))
            }
        }
    }
}

/** 5 mức hoạt động cố định theo đề — level khớp activity_level 1..5 lưu trong User. */
private enum class ActivityLevelOption(val level: Int, val titleRes: Int, val descriptionRes: Int) {
    SEDENTARY(1, R.string.activity_level_sedentary_title, R.string.activity_level_sedentary_desc),
    LIGHT(2, R.string.activity_level_light_title, R.string.activity_level_light_desc),
    MODERATE(3, R.string.activity_level_moderate_title, R.string.activity_level_moderate_desc),
    ACTIVE(4, R.string.activity_level_active_title, R.string.activity_level_active_desc),
    VERY_ACTIVE(5, R.string.activity_level_very_active_title, R.string.activity_level_very_active_desc),
}

@Composable
private fun ActivityLevelCard(
    option: ActivityLevelOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(option.titleRes), style = MaterialTheme.typography.titleMedium)
                Text(
                    stringResource(option.descriptionRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (selected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(4.dp),
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                )
            }
        }
    }
}
