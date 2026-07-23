package com.example.healthtracker.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.healthtracker.ui.component.overlay.ConfirmDeleteDialog
import com.example.healthtracker.ui.settings.components.*
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.spacing

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
        onTestReminderClick = viewModel::onTestReminderClick,
        onResetData = viewModel::onResetData,
        onAutostartReminderDialogDismissed = viewModel::onAutostartReminderDialogDismissed,
        modifier = modifier,
    )
}

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
    onTestReminderClick: () -> Unit,
    onResetData: () -> Unit,
    onAutostartReminderDialogDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val settings = uiState.settings

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.lg, vertical = MaterialTheme.spacing.lg)
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

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

        SectionTitle(title = stringResource(R.string.section_language))
        SettingsCard {
            Text(
                text = stringResource(R.string.label_app_language),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
            CustomSegmentedControl(
                options = Language.entries.map { stringResource(languageLabelRes(it)) },
                selectedIndex = Language.entries.indexOf(settings.language),
                onOptionSelected = { index -> onLanguageChange(Language.entries[index]) }
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

        SectionTitle(title = stringResource(R.string.section_appearance))
        SettingsCard {
            Text(
                text = stringResource(R.string.label_brightness),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
            CustomSegmentedControl(
                options = Brightness.entries.map { stringResource(brightnessLabelRes(it)) },
                selectedIndex = Brightness.entries.indexOf(settings.brightness),
                onOptionSelected = { index -> onBrightnessChange(Brightness.entries[index]) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = MaterialTheme.spacing.lg), color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = stringResource(R.string.label_color_theme),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
            ColorThemeSelector(
                colors = ThemePreset.entries.map { it.seedColor },
                selectedIndex = ThemePreset.entries.indexOf(settings.themePreset),
                onColorSelected = { index -> onThemePresetChange(ThemePreset.entries[index]) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = MaterialTheme.spacing.lg), color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = stringResource(R.string.label_font_size),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
            CustomSegmentedControl(
                options = FontSize.entries.map { stringResource(fontSizeLabelRes(it)) },
                selectedIndex = FontSize.entries.indexOf(settings.fontSize),
                onOptionSelected = { index -> onFontSizeChange(FontSize.entries[index]) }
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

        SectionTitle(title = stringResource(R.string.section_reminders))
        SettingsCard(padding = MaterialTheme.spacing.none) {
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
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Box(modifier = Modifier.padding(MaterialTheme.spacing.lg)) {
                OutlinedButton(
                    onClick = onTestReminderClick,
                    enabled = settings.remindersEnabled,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.action_test_reminder))
                }
            }
        }

        if (uiState.showAutostartReminderDialog) {

            val dialogTitle = stringResource(R.string.dialog_autostart_reminder_title)
            val dialogMessage = stringResource(R.string.dialog_autostart_reminder_message)
            val localContext = LocalContext.current
            val localConfiguration = LocalConfiguration.current

            AlertDialog(
                onDismissRequest = onAutostartReminderDialogDismissed,
                title = { Text(dialogTitle) },
                text = { Text(dialogMessage) },
                confirmButton = {
                    CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                        TextButton(onClick = onAutostartReminderDialogDismissed) {
                            Text(stringResource(R.string.action_got_it))
                        }
                    }
                },
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

        SectionTitle(title = stringResource(R.string.section_data_management))
        SettingsCard {
            Text(
                text = stringResource(R.string.text_reset_data_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

            var showResetConfirm by remember { mutableStateOf(false) }
            OutlinedButton(
                onClick = { showResetConfirm = true },
                enabled = !uiState.isResetting,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = BorderStroke(MaterialTheme.borderWidths.borderThin, MaterialTheme.colorScheme.error),
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

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))

        Text(
            text = stringResource(R.string.settings_app_version),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))
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
            onTestReminderClick = {},
            onResetData = {},
            onAutostartReminderDialogDismissed = {},
        )
    }
}
