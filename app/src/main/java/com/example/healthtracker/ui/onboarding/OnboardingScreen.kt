package com.example.healthtracker.ui.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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


    // Onboarding sẽ tự collect cái event này để còn chuyển hướng sang dashboard
    LaunchedEffect(Unit) {
        viewModel.completedEvent.collect { onFinishOnboarding() }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.onboarding_title)) })
        },
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
