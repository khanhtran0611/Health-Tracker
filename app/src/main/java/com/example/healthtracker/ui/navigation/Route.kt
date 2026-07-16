package com.example.healthtracker.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.example.healthtracker.domain.model.MealType
import kotlinx.serialization.Serializable

/**
 * Mọi đích điều hướng trong app. `NavKey` để backstack Nav3 nhận diện; `@Serializable`
 * để backstack save/restore khi xoay máy / process death.
 *
 * Đích không tham số → `data object`; đích có tham số → `data class`.
 * Nhờ equality theo giá trị, logic chống trùng trong backstack hoạt động chính xác
 * (vd AddEditMealEntry(5) khác AddEditMealEntry(6)).
 */
sealed interface Route : NavKey {

    // ----- Ngoài shell (không có bottom nav) -----
    @Serializable
    data object Onboarding : Route

    // ----- 5 tab bottom nav -----
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

    // ----- Màn con của Meal Diary -----
    /** entryId = null → thêm mới; có id → sửa. */
    @Serializable
    data class AddEditMealEntry(val entryId: Long? = null) : Route

    /** mealType để biết món chọn xong sẽ log vào bữa nào. */
    @Serializable
    data class FoodPicker(val mealType: MealType) : Route

    @Serializable
    data object EnterFoodManually : Route

    // ----- Màn con của Activity Diary -----
    @Serializable
    data class AddEditActivityEntry(val entryId: Long? = null) : Route

    @Serializable
    data object ChooseActivity : Route

    @Serializable
    data object EnterActivityManually : Route

    // ----- Màn con của Profile -----
    @Serializable
    data object Settings : Route

    @Serializable
    data object EditProfile : Route
}
