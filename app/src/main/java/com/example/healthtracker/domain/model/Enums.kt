package com.example.healthtracker.domain.model

import kotlinx.serialization.Serializable

enum class Gender {
    MALE,
    FEMALE,
}

enum class Goal {
    LOSE,
    MAINTAIN,
    GAIN,
}

@Serializable
enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
}

enum class Brightness {
    LIGHT,
    DARK,
    SYSTEM,
}
