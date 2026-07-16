package com.example.healthtracker.ui.mealdiary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.MealEntry
import com.example.healthtracker.domain.model.MealType
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate

/** Điểm vào thật — nối ViewModel qua Hilt. Phần hiển thị thật nằm ở [MealDiaryContent]. */
@Composable
fun MealDiaryScreen(
    viewModel: MealDiaryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MealDiaryContent(
        uiState = uiState,
        onPreviousDay = viewModel::onPreviousDay,
        onNextDay = viewModel::onNextDay,
        onDateSelected = viewModel::onDateSelected,
    )
}

/**
 * Phần hiển thị THUẦN, không đụng ViewModel/Hilt — tách riêng khỏi [MealDiaryScreen]
 * để @Preview dùng được (màn Preview không có Hilt container để dựng ViewModel thật).
 */
@Composable
fun MealDiaryContent(
    uiState: MealDiaryUiState,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.meal_diary_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        DateNavigator(
            selectedDate = uiState.selectedDate,
            onPreviousDay = onPreviousDay,
            onNextDay = onNextDay,
            onDateSelected = onDateSelected,
        )

        MealType.entries.forEach { mealType ->
            MealTypeSection(
                mealType = mealType,
                entries = uiState.entriesByMealType.getValue(mealType),
                totalCalories = uiState.totalCaloriesByMealType.getValue(mealType),
                onAddFood = { /* TODO: điều hướng sang Add Meal Entry khi làm tới */ },
                onDeleteEntry = { /* TODO: nối viewModel xoá entry khi làm tới */ },
            )
        }

        TotalTodayCard(totalCalories = uiState.totalCaloriesToday)
    }
}

@Preview(showBackground = true)
@Composable
private fun MealDiaryContentPreview() {
    HealthTrackerTheme {
        MealDiaryContent(
            uiState = MealDiaryUiState(
                selectedDate = LocalDate.now(),
                entriesByMealType = mapOf(
                    MealType.BREAKFAST to listOf(
                        MealEntry(
                            id = 1,
                            foodId = 1,
                            logDate = LocalDate.now(),
                            mealType = MealType.BREAKFAST,
                            foodName = "Oatmeal with berries",
                            quantity = 1.0,
                            calories = 280.0,
                        ),
                        MealEntry(
                            id = 2,
                            foodId = 2,
                            logDate = LocalDate.now(),
                            mealType = MealType.BREAKFAST,
                            foodName = "Black Coffee",
                            quantity = 1.0,
                            calories = 2.0,
                        ),
                    ),
                    MealType.LUNCH to listOf(
                        MealEntry(
                            id = 3,
                            foodId = 3,
                            logDate = LocalDate.now(),
                            mealType = MealType.LUNCH,
                            foodName = "Grilled Chicken Salad",
                            quantity = 1.0,
                            calories = 450.0,
                        ),
                    ),
                    MealType.DINNER to emptyList(),
                    MealType.SNACK to listOf(
                        MealEntry(
                            id = 4,
                            foodId = 4,
                            logDate = LocalDate.now(),
                            mealType = MealType.SNACK,
                            foodName = "Greek Yogurt",
                            quantity = 1.0,
                            calories = 100.0,
                        ),
                    ),
                ),
                totalCaloriesByMealType = mapOf(
                    MealType.BREAKFAST to 282.0,
                    MealType.LUNCH to 450.0,
                    MealType.DINNER to 0.0,
                    MealType.SNACK to 100.0,
                ),
                totalCaloriesToday = 832.0,
            ),
            onPreviousDay = {},
            onNextDay = {},
            onDateSelected = {},
        )
    }
}
