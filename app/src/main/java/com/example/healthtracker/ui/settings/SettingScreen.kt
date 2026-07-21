package com.example.healthtracker.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.AppSettings
import com.example.healthtracker.domain.model.Brightness
import com.example.healthtracker.domain.model.FontSize
import com.example.healthtracker.domain.model.Language
import com.example.healthtracker.domain.model.ThemePreset
import com.example.healthtracker.ui.component.ConfirmDeleteDialog
import com.example.healthtracker.ui.theme.HealthTrackerTheme

/** Điểm vào thật — nối ViewModel qua Hilt. Phần hiển thị thật nằm ở [SettingContent]. */
@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    onResetComplete: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.resetEvent.collect { onResetComplete() }
    }

    SettingContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onLanguageChange = viewModel::onLanguageChange,
        onBrightnessChange = viewModel::onBrightnessChange,
        onThemePresetChange = viewModel::onThemePresetChange,
        onFontSizeChange = viewModel::onFontSizeChange,
        onRemindersEnabledChange = viewModel::onRemindersEnabledChange,
        onMorningReminderChange = viewModel::onMorningReminderChange,
        onNoonReminderChange = viewModel::onNoonReminderChange,
        onEveningReminderChange = viewModel::onEveningReminderChange,
        onResetData = viewModel::onResetData,
        modifier = modifier,
    )
}

/**
 * Phần hiển thị THUẦN, không đụng ViewModel/Hilt — tách riêng khỏi [SettingScreen]
 * để @Preview dùng được (màn Preview không có Hilt container để dựng ViewModel thật).
 */
@Composable
fun SettingContent(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onLanguageChange: (Language) -> Unit,
    onBrightnessChange: (Brightness) -> Unit,
    onThemePresetChange: (ThemePreset) -> Unit,
    onFontSizeChange: (FontSize) -> Unit,
    onRemindersEnabledChange: (Boolean) -> Unit,
    onMorningReminderChange: (Boolean) -> Unit,
    onNoonReminderChange: (Boolean) -> Unit,
    onEveningReminderChange: (Boolean) -> Unit,
    onResetData: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val settings = uiState.settings

    // Từ Android 13 (API 33) phải có quyền POST_NOTIFICATIONS mới hiện được
    // notification — xin quyền ngay lúc user bật switch tổng, không xin trước lúc
    // chưa cần tới. Trên API < 33 (hoặc đã có quyền rồi) thì bật thẳng, khỏi hỏi.
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted -> onRemindersEnabledChange(granted) }

    fun onRemindersToggle(enabled: Boolean) {
        val needsPermission = enabled &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        if (needsPermission) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onRemindersEnabledChange(enabled)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // TopBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Spacer(modifier = Modifier.height(16.dp))

        // Language Section
        SectionTitle(title = stringResource(R.string.section_language))
        SettingsCard {
            Text(
                text = stringResource(R.string.label_app_language),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomSegmentedControl(
                options = Language.entries.map { stringResource(languageLabelRes(it)) },
                selectedIndex = Language.entries.indexOf(settings.language),
                onOptionSelected = { index -> onLanguageChange(Language.entries[index]) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Appearance Section
        SectionTitle(title = stringResource(R.string.section_appearance))
        SettingsCard {
            Text(
                text = stringResource(R.string.label_brightness),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomSegmentedControl(
                options = Brightness.entries.map { stringResource(brightnessLabelRes(it)) },
                selectedIndex = Brightness.entries.indexOf(settings.brightness),
                onOptionSelected = { index -> onBrightnessChange(Brightness.entries[index]) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = stringResource(R.string.label_color_theme),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            ColorThemeSelector(
                colors = ThemePreset.entries.map { it.seedColor },
                selectedIndex = ThemePreset.entries.indexOf(settings.themePreset),
                onColorSelected = { index -> onThemePresetChange(ThemePreset.entries[index]) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = stringResource(R.string.label_font_size),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomSegmentedControl(
                options = FontSize.entries.map { stringResource(fontSizeLabelRes(it)) },
                selectedIndex = FontSize.entries.indexOf(settings.fontSize),
                onOptionSelected = { index -> onFontSizeChange(FontSize.entries[index]) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        // Reminders Section — 3 khung giờ cố định theo đề (7h sáng/12h trưa/19h tối).
        SectionTitle(title = stringResource(R.string.section_reminders))
        SettingsCard(padding = 0.dp) {
            SwitchRow(
                title = stringResource(R.string.label_daily_reminders),
                checked = settings.remindersEnabled,
                onCheckedChange = { onRemindersToggle(it) },
                isBold = true,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SwitchRow(
                title = stringResource(R.string.label_reminder_morning),
                checked = settings.morningReminderEnabled,
                onCheckedChange = onMorningReminderChange,
                isBold = false,
                enabled = settings.remindersEnabled,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SwitchRow(
                title = stringResource(R.string.label_reminder_noon),
                checked = settings.noonReminderEnabled,
                onCheckedChange = onNoonReminderChange,
                isBold = false,
                enabled = settings.remindersEnabled,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SwitchRow(
                title = stringResource(R.string.label_reminder_evening),
                checked = settings.eveningReminderEnabled,
                onCheckedChange = onEveningReminderChange,
                isBold = false,
                enabled = settings.remindersEnabled,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Data management Section — đặt lại dữ liệu (destructive, có dialog xác nhận).
        SectionTitle(title = stringResource(R.string.section_data_management))
        SettingsCard {
            Text(
                text = stringResource(R.string.text_reset_data_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(12.dp))

            var showResetConfirm by remember { mutableStateOf(false) }
            OutlinedButton(
                onClick = { showResetConfirm = true },
                enabled = !uiState.isResetting,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.action_reset_data))
            }

            if (showResetConfirm) {
                ConfirmDeleteDialog(
                    title = stringResource(R.string.dialog_reset_data_title),
                    message = stringResource(R.string.dialog_reset_data_message),
                    onConfirm = {
                        showResetConfirm = false
                        onResetData()
                    },
                    onDismiss = { showResetConfirm = false },
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Footer
        Text(
            text = stringResource(R.string.settings_app_version),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

private fun languageLabelRes(language: Language): Int = when (language) {
    Language.EN -> R.string.language_english
    Language.VI -> R.string.language_vietnamese
}

private fun brightnessLabelRes(brightness: Brightness): Int = when (brightness) {
    Brightness.LIGHT -> R.string.brightness_light
    Brightness.DARK -> R.string.brightness_dark
    Brightness.SYSTEM -> R.string.brightness_system
}

private fun fontSizeLabelRes(fontSize: FontSize): Int = when (fontSize) {
    FontSize.SMALL -> R.string.font_size_small
    FontSize.MEDIUM -> R.string.font_size_medium
    FontSize.LARGE -> R.string.font_size_large
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsCard(
    padding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(padding),
            content = content
        )
    }
}

@Composable
private fun CustomSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, text ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.surfaceContainerLowest else Color.Transparent)
                        .clickable { onOptionSelected(index) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorThemeSelector(
    colors: List<Color>,
    selectedIndex: Int,
    onColorSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        colors.forEachIndexed { index, color ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) color else Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .background(color, CircleShape)
                    .clickable { onColorSelected(index) }
            )
        }
    }
}

@Composable
private fun SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isBold: Boolean,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingContentPreview() {
    HealthTrackerTheme {
        SettingContent(
            uiState = SettingsUiState(settings = AppSettings()),
            onBackClick = {},
            onLanguageChange = {},
            onBrightnessChange = {},
            onThemePresetChange = {},
            onFontSizeChange = {},
            onRemindersEnabledChange = {},
            onMorningReminderChange = {},
            onNoonReminderChange = {},
            onEveningReminderChange = {},
            onResetData = {},
        )
    }
}
