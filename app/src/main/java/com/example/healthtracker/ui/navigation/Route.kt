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
    // "Thêm món vào bữa" (chọn food -> nhập số lượng -> lưu) là 1 ModalBottomSheet
    // hiện qua state cục bộ (remember { mutableStateOf<Food?>(null) }) ngay trong
    // FoodPickerScreen — KHÔNG phải Route riêng. Modal thật sự không cần cả 1 Route/
    // backstack entry, chỉ cần show/hide trong đúng màn đang đứng.

    /** mealType + logDate để mang tiếp sang lúc tạo MealEntry sau khi chọn món xong. */
    @Serializable
    data class FoodPicker(val mealType: MealType, val logDate: String) : Route

    /**
     * food = null -> thêm món mới; food != null -> sửa món đã có trong catalog.
     * Truyền THẲNG object Food (không phải foodId) vì nơi gọi (Food Picker) đã có
     * sẵn Food trong tay — khỏi tốn thêm 1 lần query lại từ repository.
     */
    @Serializable
    data class EnterFoodManually(val food: Food? = null) : Route

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
