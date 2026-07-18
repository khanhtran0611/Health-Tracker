package com.example.healthtracker.ui.mealdiary.enterfood

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

    /** id của food đang sửa (null = đang thêm mới) — chỉ dùng nội bộ lúc Save. */
    private var foodId: Long? = null

    // One-shot event: lưu xong. UI tự quyết định đóng màn khi nhận được.
    private val _savedEvent = Channel<Unit>(Channel.BUFFERED)
    val savedEvent: Flow<Unit> = _savedEvent.receiveAsFlow()

    /**
     * food = null -> thêm mới (form rỗng); food khác null -> sửa, điền sẵn form
     * NGAY từ chính object đã có, KHÔNG query lại FoodRepository — nơi gọi (Food
     * Picker) đã có sẵn Food trong tay rồi.
     *
     * Gán lại NGUYÊN state (không .copy() lên state cũ) vì Composable có thể tái
     * dùng cùng 1 ViewModel instance cho nhiều lần mở modal khác nhau (khi vẫn còn
     * đứng ở Food Picker) — reset sạch để không dính quantity/lỗi của lần trước.
     */
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
                throw e // huỷ coroutine bình thường (vd rời màn) — không phải lỗi, không báo
            } catch (e: Exception) {
                toastController.show(ToastMessage(R.string.msg_food_save_failed, ToastType.ERROR))
                _uiState.update { it.copy(isSaving = false) }
                // KHÔNG gửi savedEvent -> màn không đóng, user sửa/thử lại được.
            }
        }
    }
}

/** vd 130.0 -> "130", 130.5 -> "130.5" — bỏ ".0" thừa khi là số nguyên. */
private fun formatCalories(calories: Double): String {
    return if (calories == calories.toInt().toDouble()) {
        calories.toInt().toString()
    } else {
        calories.toString()
    }
}
