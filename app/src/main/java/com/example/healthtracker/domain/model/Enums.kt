package com.example.healthtracker.domain.model

/** Giới tính — quyết định công thức BMR (nam +5 / nữ −161). */
enum class Gender {
    MALE,
    FEMALE,
}

/** Mục tiêu cân nặng — điều chỉnh TDEE (−500 / +0 / +500). */
enum class Goal {
    LOSE,
    MAINTAIN,
    GAIN,
}

/** Loại bữa ăn cho một dòng nhật ký. */
enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
}
