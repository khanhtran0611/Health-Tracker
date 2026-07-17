package com.example.healthtracker.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.example.healthtracker.domain.model.Food
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
    /**
     * Luôn tới từ Food Picker — đã biết chắc món/bữa/ngày trước khi mở màn này
     * (chỉ "thêm", chưa có luồng sửa entry cũ nên không đặt tên AddEdit).
     * logDate lưu String (ISO yyyy-MM-dd) vì java.time.LocalDate không tự
     * serialize qua Nav3 (không phải @Serializable, không phải type của
     * kotlinx.serialization) — parse lại LocalDate ở ViewModel.
     */
    @Serializable
    data class AddMealEntry(val food: Food, val mealType: MealType, val logDate: String) : Route

    /** mealType + logDate để mang tiếp sang AddMealEntry sau khi chọn món xong. */
    @Serializable
    data class FoodPicker(val mealType: MealType, val logDate: String) : Route

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
