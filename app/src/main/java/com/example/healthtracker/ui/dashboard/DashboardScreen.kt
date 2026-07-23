package com.example.healthtracker.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.ui.component.formatting.formatDiaryDate
import com.example.healthtracker.ui.dashboard.components.*
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate

@Composable
fun DashboardScreen(
    onAddMealClick: () -> Unit,
    onAddActivityClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        uiState = uiState,
        onAddMealClick = onAddMealClick,
        onAddActivityClick = onAddActivityClick,
        modifier = modifier,
    )
}

@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onAddMealClick: () -> Unit,
    onAddActivityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        DashboardHeader(dateText = formatDiaryDate(uiState.today))

        CalorieProgressCircle(
            caloriesLeft = uiState.remaining.toInt(),
            progress = uiState.progress,
            calorieStatus = uiState.calorieStatus,
        )

        DailyStatsRow(
            goal = uiState.tdee.toInt(),
            eaten = uiState.eatenToday.toInt(),
            burned = uiState.burnedToday.toInt(),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        CalorieSummaryCard(
            eatenToday = uiState.eatenToday.toInt(),
            burnedToday = uiState.burnedToday.toInt(),
            balance = uiState.balance.toInt(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        CalorieNeedIndicator(
            remainingCalories = uiState.remaining.toInt(),
            calorieStatus = uiState.calorieStatus,
        )

        Spacer(modifier = Modifier.height(24.dp))

        DashboardActionButtons(
            calorieStatus = uiState.calorieStatus,
            onAddMealClick = onAddMealClick,
            onAddActivityClick = onAddActivityClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    HealthTrackerTheme {
        DashboardContent(
            uiState = DashboardUiState(
                today = LocalDate.now(),
                tdee = 2500.0,
                eatenToday = 1300.0,
                burnedToday = 400.0,
            ),
            onAddMealClick = {},
            onAddActivityClick = {}
        )
    }
}
