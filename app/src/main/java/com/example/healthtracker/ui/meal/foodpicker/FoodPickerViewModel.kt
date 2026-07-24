package com.example.healthtracker.ui.meal.foodpicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.repository.FoodRepository
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
class FoodPickerViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val foods: Flow<List<Food>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) foodRepository.observeFoods() else foodRepository.searchFoods(query)
        }

    private val _uiState = MutableStateFlow(FoodPickerUiState())
    val uiState: StateFlow<FoodPickerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(_searchQuery, foods) { query, foods ->
                FoodPickerUiState(searchQuery = query, foods = foods, isLoading = false)
            }.collect { _uiState.value = it }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
