package com.example.healthtracker.ui.mealdiary.foodpicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.ui.theme.HealthTrackerTheme

/** Điểm vào thật — nối ViewModel qua Hilt. Phần hiển thị thật nằm ở [FoodPickerContent]. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodPickerScreen(
    onBack: () -> Unit,
    onFoodSelected: (Food) -> Unit,
    onEnterNewFood: () -> Unit,
    viewModel: FoodPickerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.food_picker_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
            )
        },
    ) { padding ->
        FoodPickerContent(
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onFoodSelected = onFoodSelected,
            onEnterNewFood = onEnterNewFood,
            modifier = Modifier.padding(padding),
        )
    }
}

/**
 * Phần hiển thị THUẦN, không đụng ViewModel/Hilt — tách riêng khỏi [FoodPickerScreen]
 * để @Preview dùng được.
 */
@Composable
fun FoodPickerContent(
    uiState: FoodPickerUiState,
    onSearchQueryChange: (String) -> Unit,
    onFoodSelected: (Food) -> Unit,
    onEnterNewFood: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(stringResource(R.string.food_picker_search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(uiState.foods, key = { it.id }) { food ->
                FoodListItem(food = food, onClick = { onFoodSelected(food) })
            }
            item {
                EnterNewFoodCard(
                    onClick = onEnterNewFood,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodPickerContentPreview() {
    HealthTrackerTheme {
        FoodPickerContent(
            uiState = FoodPickerUiState(
                searchQuery = "",
                foods = listOf(
                    Food(id = 1, name = "Cơm trắng", calories = 130.0, servingUnit = "100g"),
                    Food(id = 2, name = "Trứng gà luộc", calories = 78.0, servingUnit = "1 quả"),
                    Food(id = 3, name = "Phở bò tô nhỏ", calories = 350.0, servingUnit = "1 tô"),
                    Food(id = 4, name = "Bánh mì", calories = 265.0, servingUnit = "1 cái"),
                    Food(id = 5, name = "Ức gà luộc", calories = 165.0, servingUnit = "100g"),
                    Food(id = 6, name = "Sữa chua không đường", calories = 60.0, servingUnit = "1 hộp"),
                ),
            ),
            onSearchQueryChange = {},
            onFoodSelected = {},
            onEnterNewFood = {},
        )
    }
}
