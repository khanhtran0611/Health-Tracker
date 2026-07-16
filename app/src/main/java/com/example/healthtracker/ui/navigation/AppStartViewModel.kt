package com.example.healthtracker.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Màn khởi động app: chưa có hồ sơ -> Onboarding, đã có -> Dashboard. */
enum class AppStartDestination {
    LOADING,
    ONBOARDING,
    DASHBOARD,
}

@HiltViewModel
class AppStartViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    val startDestination: StateFlow<AppStartDestination> = userRepository.observeUser()
        .map { user -> if (user == null) AppStartDestination.ONBOARDING else AppStartDestination.DASHBOARD }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppStartDestination.LOADING)
}
