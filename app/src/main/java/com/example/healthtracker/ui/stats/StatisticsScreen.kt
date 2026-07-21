package com.example.healthtracker.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.example.healthtracker.ui.component.LoadingOverlay
import com.example.healthtracker.ui.stats.components.WeeklyBarChartCard
import com.example.healthtracker.ui.stats.components.WeeklySummaryCard
import com.example.healthtracker.ui.stats.components.WeeklyTrendChartCard
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate

/** Điểm vào thật — nối ViewModel qua Hilt. Phần hiển thị thật nằm ở [StatisticsContent]. */
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StatisticsContent(uiState = uiState, modifier = modifier)
}

/** Phần hiển thị THUẦN, không đụng ViewModel/Hilt — tách riêng để @Preview dùng được. */
@Composable
fun StatisticsContent(uiState: StatisticsUiState, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.tab_stats),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                WeeklyBarChartCard(dailyStats = uiState.dailyStats)
                WeeklyTrendChartCard(dailyStats = uiState.dailyStats)
                WeeklySummaryCard(
                    dailyStats = uiState.dailyStats,
                    avgEatenPerDay = uiState.avgEatenPerDay,
                    avgBurnedPerDay = uiState.avgBurnedPerDay,
                    daysGoalMet = uiState.daysGoalMet,
                    tdee = uiState.tdee,
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (uiState.isLoading) {
            LoadingOverlay(textRes = R.string.text_loading)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsScreenPreview() {
    val today = LocalDate.now()
    HealthTrackerTheme {
        StatisticsContent(
            uiState = StatisticsUiState(
                dailyStats = (0 until 7).map { offset ->
                    DailyCalorieStat(
                        date = today.minusDays((6 - offset).toLong()),
                        eaten = 1500.0 + offset * 100,
                        burned = 300.0 + offset * 20,
                    )
                },
                tdee = 2200.0,
            )
        )
    }
}
