package com.example.healthtracker.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.domain.model.User
import com.example.healthtracker.domain.repository.ActivityEntryRepository
import com.example.healthtracker.domain.repository.MealEntryRepository
import com.example.healthtracker.domain.repository.UserRepository
import com.example.healthtracker.domain.usecase.CalculateAgeUseCase
import com.example.healthtracker.domain.usecase.CalculateBmrUseCase
import com.example.healthtracker.domain.usecase.CalculateTdeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

private const val STATS_DAYS = 7L

// Giới hạn xem lịch sử — lùi quá xa thì dữ liệu thưa dần (mới ghi nhật ký gần
// đây), lùi vô hạn chỉ tổ cho query DB tốn công vô ích. Đồng thời đây cũng CHÍNH
// LÀ độ dài cửa sổ cho thẻ "Tổng kết 30 ngày qua" (monthlyFlow bên dưới) — 2 khái
// niệm trùng số ngày nên dùng chung 1 hằng số, tránh khai 2 số riêng dễ lệch nhau.
private const val MAX_HISTORY_DAYS = 30L

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mealEntryRepository: MealEntryRepository,
    private val activityEntryRepository: ActivityEntryRepository,
    private val calculateAgeUseCase: CalculateAgeUseCase,
    private val calculateBmrUseCase: CalculateBmrUseCase,
    private val calculateTdeeUseCase: CalculateTdeeUseCase,
) : ViewModel() {

    private val today: LocalDate = LocalDate.now()

    // 0 = 7 ngày gần nhất tính đến hôm nay, 1 = lùi thêm 7 ngày trước đó, v.v.
    // Đây là "trang" đang xem, không phải ngày cụ thể — nhân với STATS_DAYS mới
    // ra được endDate thật.
    private val _weekOffset = MutableStateFlow(0)

    // flatMapLatest tự huỷ combine() cũ, chạy combine() mới mỗi khi offset đổi —
    // giống foods ở FoodPickerViewModel: đổi cửa sổ ngày là đổi hẳn nguồn dữ liệu,
    // không phải chỉnh state tại chỗ. Phần dailyStats/startDate/endDate/canGoTo...
    // của UiState đến từ flow này; monthlySummary được combine() thêm vào riêng
    // ở "uiState" bên dưới (xem monthlyFlow).
    private val weeklyFlow: Flow<StatisticsUiState> = _weekOffset
        .flatMapLatest { offset ->
            val endDate = today.minusDays(offset * STATS_DAYS)
            val startDate = endDate.minusDays(STATS_DAYS - 1)

            combine(
                userRepository.observeUser(),
                mealEntryRepository.observeDailyTotalsByDateRange(startDate, endDate),
                activityEntryRepository.observeDailyTotalsByDateRange(startDate, endDate),
            ) { user, eatenByDate, burnedByDate ->
                val tdee = computeTdee(user)
                val dailyStats = (0 until STATS_DAYS).map { dayOffset ->
                    val date = startDate.plusDays(dayOffset)
                    DailyCalorieStat(
                        date = date,
                        eaten = eatenByDate[date] ?: 0.0,
                        burned = burnedByDate[date] ?: 0.0,
                    )
                }
                StatisticsUiState(
                    dailyStats = dailyStats,
                    tdee = tdee,
                    startDate = startDate,
                    endDate = endDate,
                    canGoToPreviousRange = canGoBackFrom(startDate),
                    // offset = 0 nghĩa là đang ở đúng 7 ngày gần nhất -> không có "tương lai" để tiến tới.
                    canGoToNextRange = offset > 0,
                )
            }
        }

    // Thẻ "Tổng kết 30 ngày qua" LUÔN nhìn về đúng 30 ngày gần nhất tính đến hôm
    // nay — KHÔNG phụ thuộc _weekOffset, nên tách flow riêng thay vì nhét chung
    // vào weeklyFlow (nếu chung, mỗi lần lùi/tiến tuần sẽ vô tình re-subscribe cả
    // phần 30 ngày dù số liệu đó không hề đổi).
    private val monthlyStartDate: LocalDate = today.minusDays(MAX_HISTORY_DAYS - 1)
    private val monthlyFlow: Flow<MonthlySummary> = combine(
        userRepository.observeUser(),
        mealEntryRepository.observeDailyTotalsByDateRange(monthlyStartDate, today),
        activityEntryRepository.observeDailyTotalsByDateRange(monthlyStartDate, today),
    ) { user, eatenByDate, burnedByDate ->
        val tdee = computeTdee(user)
        val stats = (0 until MAX_HISTORY_DAYS).map { dayOffset ->
            val date = monthlyStartDate.plusDays(dayOffset)
            DailyCalorieStat(date = date, eaten = eatenByDate[date] ?: 0.0, burned = burnedByDate[date] ?: 0.0)
        }
        MonthlySummary(
            avgEatenPerDay = stats.sumOf { it.eaten } / stats.size,
            avgBurnedPerDay = stats.sumOf { it.burned } / stats.size,
            daysGoalMet = stats.count { it.isGoalMet(tdee) },
            totalDays = stats.size,
        )
    }

    val uiState: StateFlow<StatisticsUiState> = combine(weeklyFlow, monthlyFlow) { weekly, monthly ->
        weekly.copy(monthlySummary = monthly)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticsUiState())

    /** BMR -> TDEE runtime từ hồ sơ user — dùng chung cho cả weeklyFlow lẫn monthlyFlow, tránh lặp code 2 nơi. */
    private fun computeTdee(user: User?): Double {
        if (user == null) return 0.0
        val age = calculateAgeUseCase.calculate(user.dateOfBirth)
        val bmr = calculateBmrUseCase.calculate(
            gender = user.gender,
            weightKg = user.weightKg,
            heightCm = user.heightCm,
            age = age,
        )
        return calculateTdeeUseCase.calculate(bmr = bmr, activityLevel = user.activityLevel, goal = user.goal)
    }

    /** Lùi thêm 7 ngày nữa (từ [startDate] hiện tại) có vượt quá giới hạn MAX_HISTORY_DAYS không. */
    private fun canGoBackFrom(startDate: LocalDate): Boolean =
        startDate.minusDays(STATS_DAYS) >= today.minusDays(MAX_HISTORY_DAYS - 1)

    fun onPreviousRange() {
        _weekOffset.update { offset ->
            val endDate = today.minusDays(offset * STATS_DAYS)
            val startDate = endDate.minusDays(STATS_DAYS - 1)
            // Tính lại canGoBackFrom ngay tại đây (không đọc uiState.value) để chặn
            // đúng lúc, không bị trễ 1 nhịp so với flow chưa kịp emit.
            if (canGoBackFrom(startDate)) offset + 1 else offset
        }
    }

    fun onNextRange() {
        _weekOffset.update { (it - 1).coerceAtLeast(0) }
    }
}
