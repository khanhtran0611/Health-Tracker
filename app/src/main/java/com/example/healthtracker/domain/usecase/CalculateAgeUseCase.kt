package com.example.healthtracker.domain.usecase

import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

class CalculateAgeUseCase @Inject constructor() {
    fun calculate(dateOfBirth: LocalDate, today: LocalDate = LocalDate.now()): Int {
        return Period.between(dateOfBirth, today).years
    }
}
