package com.example.healthtracker.ui.mealdiary.foodpicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FoodPickerViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    // Rỗng thì hiện cả catalog (observeFoods); gõ gì thì chuyển sang searchFoods(query).
    // flatMapLatest tự huỷ query cũ, chạy query mới mỗi khi searchQuery đổi.
    // Đây là luồng RIÊNG, có độ trễ do phải đợi DB — KHÔNG được dùng trực tiếp để hiển
    // thị chữ đang gõ (nếu vậy TextField sẽ bị "dội ngược" chờ DB, gây giật con trỏ).
    @OptIn(ExperimentalCoroutinesApi::class)
    private val foods: Flow<List<Food>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) foodRepository.observeFoods() else foodRepository.searchFoods(query)
        }

    // combine() phát UiState mới ngay khi MỘT TRONG HAI nguồn đổi, dùng giá trị mới
    // nhất của nguồn còn lại (kể cả khi nó chưa kịp cập nhật). Nhờ vậy searchQuery luôn
    // echo tức thời theo đúng những gì vừa gõ; chỉ có foods được phép hiện trễ vài
    // mili-giây trong lúc chờ DB — mắt không nhận ra được độ trễ này.
    val uiState: StateFlow<FoodPickerUiState> = combine(_searchQuery, foods) { query, foods ->
        FoodPickerUiState(searchQuery = query, foods = foods)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FoodPickerUiState())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
