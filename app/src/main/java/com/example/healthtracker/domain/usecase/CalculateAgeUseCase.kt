package com.example.healthtracker.domain.usecase

import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

/**
 * Tính tuổi runtime từ ngày sinh (KHÔNG lưu DB). Dùng ở Edit Profile/Onboarding
 * (hiển thị "Tuổi: X") và sau này ở Dashboard (tính BMR cần tuổi).
 */
class CalculateAgeUseCase @Inject constructor() {
    fun calculate(dateOfBirth: LocalDate, today: LocalDate = LocalDate.now()): Int {
        return Period.between(dateOfBirth, today).years
    }
}
