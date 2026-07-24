package com.example.healthtracker.ui.meal.addmealentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.model.MealEntry
import com.example.healthtracker.domain.model.MealType
import com.example.healthtracker.R
import com.example.healthtracker.domain.repository.FoodRepository
import com.example.healthtracker.domain.repository.MealEntryRepository
import com.example.healthtracker.ui.toast.ToastController
import com.example.healthtracker.ui.toast.ToastMessage
import com.example.healthtracker.ui.toast.ToastType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val MAX_QUANTITY = 50
private const val MAX_DAILY_CALORIES_EATEN = 99999.0

@HiltViewModel
class AddMealEntryViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val mealEntryRepository: MealEntryRepository,
    private val toastController: ToastController,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMealEntryUiState())
    val uiState: StateFlow<AddMealEntryUiState> = _uiState.asStateFlow()

    private var mealType: MealType = MealType.BREAKFAST

    private val _savedEvent = Channel<Unit>(Channel.BUFFERED)
    val savedEvent: Flow<Unit> = _savedEvent.receiveAsFlow()

    fun initialize(food: Food, mealType: MealType, logDate: LocalDate) {
        this.mealType = mealType
        _uiState.value = AddMealEntryUiState(food = food, logDate = logDate, mealType = mealType)
    }

    fun onDecreaseQuantity() {
        _uiState.update { it.copy(quantity = (it.quantity - 1).coerceAtLeast(1)) }
    }

    fun onIncreaseQuantity() {
        _uiState.update { it.copy(quantity = (it.quantity + 1).coerceAtMost(MAX_QUANTITY)) }
    }

    fun onSubmit() {
        val state = _uiState.value
        val food = state.food ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val newCalories = food.calories * state.quantity
                val existingTotal = mealEntryRepository.getTotalCaloriesByDate(state.logDate)
                if (existingTotal + newCalories > MAX_DAILY_CALORIES_EATEN) {
                    toastController.show(ToastMessage(R.string.msg_daily_calories_eaten_exceeded, ToastType.ERROR))
                    _uiState.update { it.copy(isSaving = false) }
                    return@launch
                }

                mealEntryRepository.addEntry(
                    MealEntry(
                        foodId = food.id,
                        logDate = state.logDate,
                        mealType = mealType,
                        foodName = food.name,
                        quantity = state.quantity,
                        calories = newCalories,
                    ),
                )
                toastController.show(ToastMessage(R.string.msg_meal_entry_added, ToastType.SUCCESS))
                _uiState.update { it.copy(isSaving = false) }
                _savedEvent.send(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                toastController.show(ToastMessage(R.string.msg_meal_entry_failed, ToastType.ERROR))
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }
}
