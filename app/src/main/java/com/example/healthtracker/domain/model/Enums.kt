package com.example.healthtracker.domain.model

import kotlinx.serialization.Serializable

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

/**
 * Loại bữa ăn cho một dòng nhật ký.
 * @Serializable để Route.FoodPicker(mealType) mang được giá trị này qua Nav3 backstack.
 */
@Serializable
enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
}

/** Chế độ sáng/tối của giao diện — SYSTEM đi theo cài đặt hệ điều hành. */
enum class Brightness {
    LIGHT,
    DARK,
    SYSTEM,
}
