package com.example.healthtracker.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import androidx.compose.ui.tooling.preview.Preview
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
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stringResource(R.string.field_full_name), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(
                value = state.fullName,
                onValueChange = onFullNameChange,
                isError = state.fullNameError != null,
                supportingText = fieldErrorText(state.fullNameError)?.let { { Text(it) } },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        DateOfBirthField(
            dateOfBirth = state.dateOfBirth,
            age = state.age,
            error = state.dateOfBirthError,
            onDateChange = onDateOfBirthChange,
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stringResource(R.string.section_gender), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            GenderSelector(gender = state.gender, onGenderChange = onGenderChange)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.field_weight), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = state.weightKg,
                    onValueChange = onWeightChange,
                    suffix = { Text(stringResource(R.string.unit_kg)) },
                    isError = state.weightError != null,
                    supportingText = fieldErrorText(state.weightError)?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.field_height), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = state.heightCm,
                    onValueChange = onHeightChange,
                    suffix = { Text(stringResource(R.string.unit_cm)) },
                    isError = state.heightError != null,
                    supportingText = fieldErrorText(state.heightError)?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
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

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(stringResource(R.string.field_date_of_birth), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedTextField(
            value = dateOfBirth?.format(formatter) ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            isError = error != null,
            supportingText = fieldErrorText(error)?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
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
        // DatePickerDialog dựng Dialog riêng, bên trong tự lấy lại LocalContext/
        // LocalConfiguration từ Window thật (Activity gốc) chứ không kế thừa bản
        // đã đổi ngôn ngữ của LocalizedApp -> bắt lại 2 Local này ở NGOÀI (đúng
        // ngôn ngữ) rồi re-provide vào các slot bên trong.
        val localContext = LocalContext.current
        val localConfiguration = LocalConfiguration.current

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dateOfBirth
                ?.atStartOfDay(ZoneOffset.UTC)
                ?.toInstant()
                ?.toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            onDateChange(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                        }
                        showDatePicker = false
                    }) {
                        Text(stringResource(R.string.action_confirm))
                    }
                }
            },
            dismissButton = {
                CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.action_cancel))
                    }
                }
            },
        ) {
            CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderSelector(gender: Gender, onGenderChange: (Gender) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(Gender.MALE to R.string.gender_male, Gender.FEMALE to R.string.gender_female)
    val selectedText = stringResource(options.first { it.first == gender }.second)

    // ExposedDropdownMenu dựng Popup riêng, bên trong tự lấy lại LocalContext/
    // LocalConfiguration từ Window thật (Activity gốc) chứ không kế thừa bản đã
    // đổi ngôn ngữ của LocalizedApp -> bắt lại 2 Local này ở NGOÀI (đúng ngôn ngữ)
    // rồi re-provide vào các slot bên trong dùng stringResource().
    val localContext = LocalContext.current
    val localConfiguration = LocalConfiguration.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                options.forEach { (option, labelRes) ->
                    DropdownMenuItem(
                        text = { Text(stringResource(labelRes)) },
                        onClick = {
                            onGenderChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/** Cấu hình hiển thị cho 1 lựa chọn Goal — gộp icon xu hướng + text res vào 1 chỗ cho gọn. */
private data class GoalOption(
    val goal: Goal,
    val titleRes: Int,
    val descriptionRes: Int,
    val icon: ImageVector,
)

/**
 * Bộ chọn Goal (Lose/Maintain/Gain weight) — mỗi lựa chọn nằm riêng 1 hàng dọc
 * (giống cấu trúc Column của ActivityLevelCard bên dưới: xếp dọc, full-width),
 * NHƯNG cố tình thiết kế khác để 2 khối không bị lẫn với nhau dù đứng cạnh nhau
 * trong form:
 *   - ActivityLevelCard: text bên trái, icon check/circle bên phải.
 *   - GoalOption ở đây: icon xu hướng trong vòng tròn màu BÊN TRÁI (mũi tên
 *     xuống/ngang/lên tương ứng giảm/giữ/tăng cân), có thêm dòng phụ mô tả tác
 *     động lên TDEE (đúng công thức đã chốt ở CLAUDE.md: -500 / +0 / +500 kcal),
 *     và dùng RadioButton chuẩn Material3 bên phải thay vì icon check tự vẽ.
 *
 * Nhờ mỗi lựa chọn chiếm trọn 1 hàng full-width (không còn ép 3 ô chung 1 hàng
 * như SegmentedButton trước đây), chữ "Maintain weight" luôn có đủ chỗ nằm gọn
 * 1 dòng — không còn lỗi tràn/wrap chữ ra ngoài khung nữa.
 */
@Composable
private fun GoalSelector(goal: Goal, onGoalChange: (Goal) -> Unit) {
    val options = listOf(
        GoalOption(Goal.LOSE, R.string.goal_lose, R.string.goal_lose_desc, Icons.Filled.TrendingDown),
        GoalOption(Goal.MAINTAIN, R.string.goal_maintain, R.string.goal_maintain_desc, Icons.Filled.TrendingFlat),
        GoalOption(Goal.GAIN, R.string.goal_gain, R.string.goal_gain_desc, Icons.Filled.TrendingUp),
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val selected = goal == option.goal
            Surface(
                onClick = { onGoalChange(option.goal) },
                shape = RoundedCornerShape(16.dp),
                color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(
                    width = if (selected) 1.5.dp else 1.dp,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = option.icon,
                            contentDescription = null,
                            tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(option.titleRes),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(option.descriptionRes),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    RadioButton(
                        selected = selected,
                        onClick = { onGoalChange(option.goal) },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary),
                    )
                }
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

/** Tên hiển thị (string res) của 1 mức activity_level 1..5 — dùng lại ở Profile để hiện "Dựa trên: <mức>". */
fun activityLevelTitleRes(level: Int): Int =
    ActivityLevelOption.entries.first { it.level == level }.titleRes

@Composable
private fun ActivityLevelCard(
    option: ActivityLevelOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
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
                Text(stringResource(option.titleRes), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    stringResource(option.descriptionRes),
                    style = MaterialTheme.typography.bodySmall,
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

@Preview(showBackground = true)
@Composable
private fun ProfileFormPreview() {
    HealthTrackerTheme {
        ProfileForm(
            state = ProfileFormUiState(
                isLoading = false,
                fullName = "Alex Johnson",
                dateOfBirth = LocalDate.of(1996, 5, 20),
                age = 28,
                gender = Gender.MALE,
                weightKg = "70",
                heightCm = "175",
                activityLevel = 3,
                goal = Goal.MAINTAIN,
                isSaving = false,
                fullNameError = null,
                dateOfBirthError = null,
                weightError = null,
                heightError = null
            ),
            onFullNameChange = {},
            onDateOfBirthChange = {},
            onGenderChange = {},
            onWeightChange = {},
            onHeightChange = {},
            onActivityLevelChange = {},
            onGoalChange = {},
            onSubmit = {},
            submitLabel = "Save"
        )
    }
}
