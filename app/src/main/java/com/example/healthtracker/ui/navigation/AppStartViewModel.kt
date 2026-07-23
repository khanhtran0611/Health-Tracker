package com.example.healthtracker.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AppStartDestination {
    LOADING,
    ONBOARDING,
    MAIN_SHELL,
}

@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _startDestination = MutableStateFlow(AppStartDestination.LOADING)
    val startDestination: StateFlow<AppStartDestination> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.observeUser()
                .map { user -> if (user == null) AppStartDestination.ONBOARDING else AppStartDestination.MAIN_SHELL }
                .collect { _startDestination.value = it }
        }
    }
}
