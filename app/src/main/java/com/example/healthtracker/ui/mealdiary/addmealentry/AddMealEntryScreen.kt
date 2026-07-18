package com.example.healthtracker.ui.mealdiary.addmealentry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.model.MealType
import com.example.healthtracker.ui.component.formatDiaryDate
import com.example.healthtracker.ui.theme.HealthTrackerTheme
import java.time.LocalDate

/**
 * Điểm vào thật — nối ViewModel qua Hilt, hiển thị dạng ModalBottomSheet (trượt lên
 * từ dưới, tự làm tối nền, tự có drag handle). Vẫn nằm trong backstack chung như
 * mọi Route khác — [onDismiss] chỉ đơn giản gọi backStack.removeLastOrNull(), không
 * phát sinh cơ chế điều hướng riêng nào.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealEntryScreen(
    food : Food,
    mealType: MealType,
    logDate: LocalDate,
    onDismiss: () -> Unit,
    viewModel: AddMealEntryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(food, mealType, logDate) {
        viewModel.initialize(food, mealType, logDate)
    }

    LaunchedEffect(Unit) {
        viewModel.savedEvent.collect { onDismiss() }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        AddMealEntryContent(
            uiState = uiState,
            onDecreaseQuantity = viewModel::onDecreaseQuantity,
            onIncreaseQuantity = viewModel::onIncreaseQuantity,
            onSave = viewModel::onSubmit,
            onClose = onDismiss,
        )
    }
}

/**
 * Phần hiển thị THUẦN, không đụng ViewModel/Hilt — tách riêng để @Preview dùng được.
 */
@Composable
fun AddMealEntryContent(
    uiState: AddMealEntryUiState,
    onDecreaseQuantity: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onSave: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        // Header "✕  Add food  Save" — Box+Alignment để tiêu đề luôn ở giữa,
        // giống kỹ thuật đã dùng ở DateNavigator/MealDiaryScreen.
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.action_close))
            }
            Text(
                stringResource(R.string.add_meal_entry_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
            TextButton(
                onClick = onSave,
                enabled = uiState.food != null && !uiState.isSaving,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.padding(end = 8.dp))
                }
                Text(stringResource(R.string.action_save))
            }
        }

        Spacer(Modifier.height(16.dp))

        val food = uiState.food
        if (food == null) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            FoodSummaryCard(food = food)

            Spacer(Modifier.height(24.dp))
            Text(stringResource(R.string.label_quantity), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            QuantityStepper(
                quantity = uiState.quantity,
                onDecrease = onDecreaseQuantity,
                onIncrease = onIncreaseQuantity,
            )

            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    stringResource(R.string.label_logging_for),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(formatDiaryDate(uiState.logDate), style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(16.dp))
            TotalForEntryCard(totalCalories = uiState.totalCalories)
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMealEntryContentPreview() {
    HealthTrackerTheme {
        AddMealEntryContent(
            uiState = AddMealEntryUiState(
                food = Food(id = 1, name = "Phở bò tô nhỏ", calories = 350.0, servingUnit = "1 tô"),
                quantity = 1.0,
                logDate = LocalDate.now(),
            ),
            onDecreaseQuantity = {},
            onIncreaseQuantity = {},
            onSave = {},
            onClose = {},
        )
    }
}
