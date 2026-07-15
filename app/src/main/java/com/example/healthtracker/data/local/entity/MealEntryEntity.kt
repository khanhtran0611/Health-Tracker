package com.example.healthtracker.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.healthtracker.domain.model.MealType
import java.time.LocalDate

/**
 * Một dòng nhật ký ăn. `foodName` + `calories` là SNAPSHOT tại thời điểm log —
 * sửa catalog `foods` sau này không được làm đổi số liệu ở đây.
 * FK food_id → foods (RESTRICT: không cho xóa food đang được entry tham chiếu).
 */
@Entity(
    tableName = "meal_entries",
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["food_id"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["food_id"]),
        Index(value = ["log_date"]),
        Index(value = ["log_date", "meal_type"]),
    ],
)
data class MealEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Luôn trỏ tới 1 row trong foods (NOT NULL, không bao giờ free-text). */
    @ColumnInfo(name = "food_id")
    val foodId: Long,

    @ColumnInfo(name = "log_date")
    val logDate: LocalDate,

    @ColumnInfo(name = "meal_type")
    val mealType: MealType,

    /** Snapshot tên món lúc log. */
    @ColumnInfo(name = "food_name")
    val foodName: String,

    /** Số khẩu phần. */
    val quantity: Double = 1.0,

    /** Snapshot = food.calories * quantity. */
    val calories: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)
