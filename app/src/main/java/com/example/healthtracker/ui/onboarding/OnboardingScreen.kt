package com.example.healthtracker.ui.onboarding

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.ui.component.ProfileForm

/**
 * Onboarding = ProfileForm KHÔNG có nút back, nút submit là "Next" (đúng CLAUDE.md).
 * Sau khi lưu xong, gọi [onFinishOnboarding] để HealthTrackerApp xoá backstack
 * và chuyển vào Dashboard — Onboarding tự nó không biết Route.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()

    // Onboarding sẽ tự collect cái event này để còn chuyển hướng sang dashboard
    LaunchedEffect(Unit) {
        viewModel.completedEvent.collect { onFinishOnboarding() }
    }

    // Outer Scaffold (HealthTrackerNavHost) đã chừa insets hệ thống (status bar/nav bar)
    // 1 lần rồi -> Scaffold + TopAppBar ở đây phải để 0, không chừa thêm lần nữa
    // (nếu không sẽ bị dư khoảng trắng cả trên lẫn dưới).
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.onboarding_title)) },
                actions = {
                    // "VI"/"EN" là tên ngôn ngữ, cố tình không dịch qua strings.xml
                    // (giống language_english/language_vietnamese ở Settings) — bấm
                    // để đổi ngay ngôn ngữ hiện tại sang ngôn ngữ còn lại.
                    TextButton(onClick = viewModel::onToggleLanguage) {
                        Text(
                            text = language.name,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        ProfileForm(
            state = uiState,
            onFullNameChange = viewModel::onFullNameChange,
            onDateOfBirthChange = viewModel::onDateOfBirthChange,
            onGenderChange = viewModel::onGenderChange,
            onWeightChange = viewModel::onWeightChange,
            onHeightChange = viewModel::onHeightChange,
            onActivityLevelChange = viewModel::onActivityLevelChange,
            onGoalChange = viewModel::onGoalChange,
            onSubmit = viewModel::onSubmit,
            submitLabel = stringResource(R.string.action_next),
            modifier = Modifier.padding(padding),
        )
    }
}
