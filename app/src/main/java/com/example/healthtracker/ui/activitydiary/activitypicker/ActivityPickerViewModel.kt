package com.example.healthtracker.ui.activitydiary.activitypicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.domain.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityPickerViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    // Rỗng thì hiện cả catalog (observeActivities); gõ gì thì chuyển sang searchActivities(query).
    // flatMapLatest tự huỷ query cũ, chạy query mới mỗi khi searchQuery đổi.
    // Đây là luồng RIÊNG, có độ trễ do phải đợi DB — KHÔNG được dùng trực tiếp để hiển
    // thị chữ đang gõ (nếu vậy TextField sẽ bị "dội ngược" chờ DB, gây giật con trỏ).
    @OptIn(ExperimentalCoroutinesApi::class)
    private val activities: Flow<List<Activity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) activityRepository.observeActivities() else activityRepository.searchActivities(query)
        }

    private val _uiState = MutableStateFlow(ActivityPickerUiState())
    val uiState: StateFlow<ActivityPickerUiState> = _uiState.asStateFlow()

    // combine() phát UiState mới ngay khi MỘT TRONG HAI nguồn đổi, dùng giá trị mới
    // nhất của nguồn còn lại (kể cả khi nó chưa kịp cập nhật). Nhờ vậy searchQuery luôn
    // echo tức thời theo đúng những gì vừa gõ; chỉ có activities được phép hiện trễ vài
    // mili-giây trong lúc chờ DB — mắt không nhận ra được độ trễ này.
    init {
        viewModelScope.launch {
            combine(_searchQuery, activities) { query, activities ->
                ActivityPickerUiState(searchQuery = query, activities = activities)
            }.collect { _uiState.value = it }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
