package com.example.healthtracker.ui.activity.activitydiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.ActivityEntry
import com.example.healthtracker.domain.repository.ActivityEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ActivityDiaryViewModel @Inject constructor(
    private val activityEntryRepository: ActivityEntryRepository
) : ViewModel() {
    private val _selectedDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())

    private val _uiState = MutableStateFlow(ActivityDiaryUiState())
    val uiState: StateFlow<ActivityDiaryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _selectedDate
                .flatMapLatest { date ->
                    activityEntryRepository.observeEntriesByDate(date).map { entries ->
                        ActivityDiaryUiState(
                            selectedDate = date,
                            entries = entries,
                            totalCaloriesBurnedToday = entries.sumOf { it.caloriesBurned },
                        )
                    }
                }
                .collect { _uiState.value = it }
        }
    }

    fun onPreviousDay() {
        _selectedDate.update { it.minusDays(1) }
    }

    fun onNextDay() {
        _selectedDate.update { it.plusDays(1) }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onDeleteEntry(entry: ActivityEntry) {
        viewModelScope.launch {
            activityEntryRepository.deleteEntry(entry)
        }
    }
}
