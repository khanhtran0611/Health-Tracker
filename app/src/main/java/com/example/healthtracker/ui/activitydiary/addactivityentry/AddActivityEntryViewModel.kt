package com.example.healthtracker.ui.activitydiary.addactivityentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.domain.model.ActivityEntry
import com.example.healthtracker.domain.repository.ActivityEntryRepository
import com.example.healthtracker.domain.repository.UserRepository
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

private const val DURATION_STEP_MINUTES = 5
private const val MIN_DURATION_MINUTES = 5

@HiltViewModel
class AddActivityEntryViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val toastController: ToastController,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddActivityEntryUiState())
    val uiState: StateFlow<AddActivityEntryUiState> = _uiState.asStateFlow()

    // One-shot event: lưu xong. UI tự quyết định đóng bottom sheet khi nhận được.
    private val _savedEvent = Channel<Unit>(Channel.BUFFERED)
    val savedEvent: Flow<Unit> = _savedEvent.receiveAsFlow()

    /**
     * Gọi mỗi lần modal mở, mang theo activity/logDate đã chọn. Khác
     * AddMealEntryViewModel.initialize() (đồng bộ, vì Food đã có sẵn đủ dữ liệu) —
     * ở đây cần thêm weightKg từ UserRepository để tính caloriesBurned, nên phải
     * launch coroutine.
     */
    fun initialize(activity: Activity, logDate: LocalDate) {
        viewModelScope.launch {
            val weightKg = userRepository.getUser()?.weightKg ?: 0.0
            _uiState.value = AddActivityEntryUiState(
                activity = activity,
                logDate = logDate,
                weightKg = weightKg,
            )
        }
    }

    // coerceAtLeast() là hàm extension của Kotlin dùng để đảm bảo
    // một giá trị không nhỏ hơn một ngưỡng tối thiểu cho trước.
    // Nếu giá trị hiện tại >= minimumValue → trả về chính giá trị đó.
    // Nếu giá trị hiện tại < minimumValue → trả về minimumValue.
    fun onDecreaseDuration() {
        _uiState.update {
            it.copy(durationMinutes = (it.durationMinutes - DURATION_STEP_MINUTES).coerceAtLeast(MIN_DURATION_MINUTES))
        }
    }

    fun onIncreaseDuration() {
        _uiState.update { it.copy(durationMinutes = it.durationMinutes + DURATION_STEP_MINUTES) }
    }

    fun onSubmit() {
        val state = _uiState.value
        val activity = state.activity ?: return
        viewModelScope.launch {
            // Điều kiện để dùng it: lambda đó phải có đúng 1 tham số
            // Còn lại trong lambda có bao nhiêu dòng lệnh cũng được
            _uiState.update { it.copy(isSaving = true) }
            try {
                activityEntryRepository.addEntry(
                    ActivityEntry(
                        activityId = activity.id,
                        logDate = state.logDate,
                        activityName = activity.name,
                        durationMinutes = state.durationMinutes,
                        caloriesBurned = state.caloriesBurned,
                    ),
                )
                toastController.show(ToastMessage(R.string.msg_activity_entry_added, ToastType.SUCCESS))
                _uiState.update { it.copy(isSaving = false) }
                _savedEvent.send(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                toastController.show(ToastMessage(R.string.msg_activity_entry_failed, ToastType.ERROR))
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }
}
