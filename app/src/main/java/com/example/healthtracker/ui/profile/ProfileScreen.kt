package com.example.healthtracker.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import com.example.healthtracker.ui.component.activityLevelTitleRes
import com.example.healthtracker.ui.profile.components.BmiCard
import com.example.healthtracker.ui.profile.components.ProfileStatsRow
import com.example.healthtracker.ui.profile.components.TdeeCard
import com.example.healthtracker.ui.theme.HealthTrackerTheme

/** Điểm vào thật — nối ViewModel qua Hilt. Phần hiển thị thật nằm ở [ProfileContent]. */
@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent(
        uiState = uiState,
        onEditProfileClick = onEditProfileClick,
        onSettingsClick = onSettingsClick,
        modifier = modifier,
    )
}

/**
 * Phần hiển thị THUẦN, không đụng ViewModel/Hilt — tách riêng khỏi [ProfileScreen]
 * để @Preview dùng được (màn Preview không có Hilt container để dựng ViewModel thật).
 */
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center),
            )
            IconButton(onClick = onSettingsClick, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(R.string.action_settings),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = uiState.fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (uiState.age != null) {
                Text(
                    text = stringResource(
                        R.string.profile_age_gender_format,
                        uiState.age,
                        stringResource(if (uiState.gender == Gender.MALE) R.string.gender_male else R.string.gender_female),
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        ProfileStatsRow(
            weightKg = uiState.weightKg.toInt(),
            heightCm = uiState.heightCm.toInt(),
            goal = uiState.goal,
        )

        BmiCard(bmi = uiState.bmi, category = uiState.bmiCategory)

        TdeeCard(tdee = uiState.tdee.toInt(), activityLevelTitleRes = activityLevelTitleRes(uiState.activityLevel))

        OutlinedButton(
            onClick = onEditProfileClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.action_edit_profile), fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileContentPreview() {
    HealthTrackerTheme {
        ProfileContent(
            uiState = ProfileUiState(
                isLoading = false,
                fullName = "Alex Johnson",
                age = 28,
                gender = Gender.MALE,
                weightKg = 70.0,
                heightCm = 175.0,
                activityLevel = 3,
                goal = Goal.MAINTAIN,
                bmi = 22.9,
                tdee = 2500.0,
            ),
            onEditProfileClick = {},
            onSettingsClick = {},
        )
    }
}
