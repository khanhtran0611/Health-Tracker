package com.example.healthtracker.ui.meal.enterfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.repository.FoodRepository
import com.example.healthtracker.ui.toast.ToastMessage
import com.example.healthtracker.ui.toast.ToastType
import com.example.healthtracker.ui.toast.ToastController
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
import javax.inject.Inject

@HiltViewModel
class EnterFoodManuallyViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val toastController: ToastController,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnterFoodManuallyUiState())
    val uiState: StateFlow<EnterFoodManuallyUiState> = _uiState.asStateFlow()

    private var foodId: Long? = null

    private val _savedEvent = Channel<Unit>(Channel.BUFFERED)
    val savedEvent: Flow<Unit> = _savedEvent.receiveAsFlow()

    fun initialize(food: Food?) {
        foodId = food?.id
        _uiState.value = EnterFoodManuallyUiState(
            name = food?.name ?: "",
            caloriesInput = food?.calories?.let(::formatCalories) ?: "",
            servingUnit = food?.servingUnit ?: "",
        )
    }

    fun onNameChange(name: String) {
        _uiState.update { validateEnterFoodManuallyForm(it.copy(name = name)) }
    }

    fun onCaloriesChange(calories: String) {
        _uiState.update { validateEnterFoodManuallyForm(it.copy(caloriesInput = calories)) }
    }

    fun onServingUnitChange(servingUnit: String) {
        _uiState.update { it.copy(servingUnit = servingUnit) }
    }

    fun onSubmit() {
        val validated = validateEnterFoodManuallyForm(_uiState.value)
        _uiState.value = validated
        if (!isEnterFoodManuallyFormValid(validated)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val food = Food(
                    id = foodId ?: 0,
                    name = validated.name,
                    calories = validated.caloriesInput.toDouble(),
                    servingUnit = validated.servingUnit.ifBlank { null },
                )
                if (foodId == null) {
                    foodRepository.addFood(food)
                } else {
                    foodRepository.updateFood(food)
                }
                toastController.show(ToastMessage(R.string.msg_food_saved, ToastType.SUCCESS))
                _uiState.update { it.copy(isSaving = false) }
                _savedEvent.send(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                toastController.show(ToastMessage(R.string.msg_food_save_failed, ToastType.ERROR))
                _uiState.update { it.copy(isSaving = false) }

            }
        }
    }

    fun onDelete() {
        val id = foodId ?: return
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            try {
                foodRepository.deleteFood(
                    Food(
                        id = id,
                        name = state.name,
                        calories = state.caloriesInput.toDoubleOrNull() ?: 0.0,
                        servingUnit = state.servingUnit.ifBlank { null },
                    ),
                )
                toastController.show(ToastMessage(R.string.msg_food_deleted, ToastType.SUCCESS))
                _uiState.update { it.copy(isDeleting = false) }
                _savedEvent.send(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                toastController.show(ToastMessage(R.string.msg_food_delete_failed, ToastType.ERROR))
                _uiState.update { it.copy(isDeleting = false) }
            }
        }
    }
}

private fun formatCalories(calories: Double): String {
    return if (calories == calories.toInt().toDouble()) {
        calories.toInt().toString()
    } else {
        calories.toString()
    }
}
