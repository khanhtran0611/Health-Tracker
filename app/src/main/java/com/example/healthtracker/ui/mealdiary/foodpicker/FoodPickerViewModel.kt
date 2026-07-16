package com.example.healthtracker.ui.mealdiary.foodpicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FoodPickerViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    // Rỗng thì hiện cả catalog (observeFoods); gõ gì thì chuyển sang searchFoods(query).
    // flatMapLatest tự huỷ query cũ, chạy query mới mỗi khi searchQuery đổi.
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FoodPickerUiState> = _searchQuery
        .flatMapLatest { query ->
            val foodsFlow = if (query.isBlank()) foodRepository.observeFoods() else foodRepository.searchFoods(query)
            foodsFlow.map { foods -> FoodPickerUiState(searchQuery = query, foods = foods) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FoodPickerUiState())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
