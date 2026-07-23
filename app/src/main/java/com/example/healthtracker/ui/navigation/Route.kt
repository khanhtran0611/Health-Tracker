package com.example.healthtracker.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.domain.model.Food
import com.example.healthtracker.domain.model.MealType
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object Onboarding : Route

    @Serializable
    data object MainShell : Route

    @Serializable
    data object Dashboard : Route

    @Serializable
    data object MealDiary : Route

    @Serializable
    data object ActivityDiary : Route

    @Serializable
    data object Stats : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data class FoodPicker(val mealType: MealType, val logDate: String) : Route

    @Serializable
    data class EnterFoodManually(val food: Food? = null) : Route

    @Serializable
    data class ChooseActivity(val logDate: String) : Route

    @Serializable
    data class EnterActivityManually(val activity: Activity? = null) : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data object EditProfile : Route
}
