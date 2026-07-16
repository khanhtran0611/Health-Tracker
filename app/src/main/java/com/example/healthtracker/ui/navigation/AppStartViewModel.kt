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

// Giải thích: Ban đầu khi mở app, startDestionation được lắng nghe
// Nếu ko có user => Onboarding, ko thì sang dashboard
// ở bên kia, do dùng rememberBackStack và cái này thì ko có mutableState để trigger thay đổi,
// nên về sau có thay đổi startRoute thì màn hình nó cũng ko thay đổi.
// với trường hợp sau khi tạo user xong, thì đã xử lý thủ công : clear backstack và add dashboard vào.
// lưu ý , dữ liệu trong stateflow ko thay đổi => ko emit và collector bên kia ko trigger recomposition được
// Như vậy HealthTrackerApp ko bị chạy lại và màn hình vẫn thế.
// Lưu ý 2: Dữ liệu gốc (kq từ observeUser() có thay đổi ra sao) => map nó vẫn sẽ chạy
// và kq sinh ra sau cùng sẽ quyết định có trùng nhau để emit ra hay không.


@HiltViewModel
class AppStartViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    val startDestination: StateFlow<AppStartDestination> = userRepository.observeUser()
        .map { user -> if (user == null) AppStartDestination.ONBOARDING else AppStartDestination.DASHBOARD }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppStartDestination.LOADING)
}
