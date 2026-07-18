package com.example.healthtracker.ui.mealdiary.addmealentry

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

/**
 * mealType không có trong UiState vì màn này không cho đổi bữa (đã cố định từ
 * lúc bấm "+ Thêm món ăn" ở đúng bữa đó bên Meal Diary) — chỉ giữ làm biến nội
 * bộ để biết log vào bữa nào lúc Save, không cần hiển thị lên UI.
 */
@HiltViewModel
class AddMealEntryViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val mealEntryRepository: MealEntryRepository,
    private val toastController: ToastController,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMealEntryUiState())
    val uiState: StateFlow<AddMealEntryUiState> = _uiState.asStateFlow()

    private var mealType: MealType = MealType.BREAKFAST

    // One-shot event: lưu xong. UI tự quyết định đóng bottom sheet khi nhận được.
    private val _savedEvent = Channel<Unit>(Channel.BUFFERED)
    val savedEvent: Flow<Unit> = _savedEvent.receiveAsFlow()

    /**
     * Gọi mỗi lần modal mở, mang theo food/mealType/logDate đã chọn.
     * Gán lại NGUYÊN state (không .copy() lên state cũ) vì FoodPickerScreen giữ
     * modal ở dạng state cục bộ — cùng 1 ViewModel instance có thể bị dùng lại
     * cho nhiều lần mở modal khác nhau (mỗi lần chọn 1 food khác) trong lúc vẫn
     * đứng ở Food Picker. Nếu chỉ .copy() thì quantity của lần chọn trước sẽ dính
     * sang lần chọn sau — phải reset sạch.
     */
    fun initialize(food: Food, mealType: MealType, logDate: LocalDate) {
        this.mealType = mealType
        _uiState.value = AddMealEntryUiState(food = food, logDate = logDate, mealType = mealType)
    }

    fun onDecreaseQuantity() {
        _uiState.update { it.copy(quantity = (it.quantity - 1).coerceAtLeast(1.0)) }
    }

    fun onIncreaseQuantity() {
        _uiState.update { it.copy(quantity = it.quantity + 1) }
    }

    fun onSubmit() {
        val state = _uiState.value
        val food = state.food ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                mealEntryRepository.addEntry(
                    MealEntry(
                        foodId = food.id,
                        logDate = state.logDate,
                        mealType = mealType,
                        foodName = food.name,
                        quantity = state.quantity,
                        calories = food.calories * state.quantity,
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
