package com.example.healthtracker.ui.mealdiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.MealEntry
import com.example.healthtracker.domain.model.MealType
import com.example.healthtracker.domain.repository.MealEntryRepository
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
class MealDiaryViewModel @Inject constructor(
    private val mealEntryRepository: MealEntryRepository
) : ViewModel(){
    private val _selectedDate : MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())

    private val _uiState = MutableStateFlow(MealDiaryUiState())
    val uiState: StateFlow<MealDiaryUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            _selectedDate
                .flatMapLatest { date ->
                    mealEntryRepository.observeEntriesByDate(date).map { entries ->
                        val entriesByMealType = MealType.entries.associateWith { type -> entries.filter { it.mealType == type } }
                        val totalCaloriesByMealType = entriesByMealType.mapValues { (_, list) ->
                            list.sumOf { it.calories }
                        }
                        MealDiaryUiState(
                            selectedDate = date,
                            entriesByMealType = entriesByMealType,
                            totalCaloriesByMealType = totalCaloriesByMealType,
                            totalCaloriesToday = entries.sumOf { it.calories },
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

    fun onDeleteEntry(entry: MealEntry) {
        viewModelScope.launch {
            mealEntryRepository.deleteEntry(entry)
        }
    }
}
