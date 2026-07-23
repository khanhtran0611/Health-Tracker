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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val activities: Flow<List<Activity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) activityRepository.observeActivities() else activityRepository.searchActivities(query)
        }

    private val _uiState = MutableStateFlow(ActivityPickerUiState())
    val uiState: StateFlow<ActivityPickerUiState> = _uiState.asStateFlow()

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
