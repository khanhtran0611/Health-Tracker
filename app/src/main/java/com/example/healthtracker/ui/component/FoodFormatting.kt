package com.example.healthtracker.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Food

/** "130 kcal / 100g", hoặc chỉ "130 kcal" nếu food không có servingUnit. */
@Composable
fun formatFoodCalorieInfo(food: Food): String {
    val kcalText = "${food.calories.toInt()} ${stringResource(R.string.unit_kcal)}"
    return if (food.servingUnit != null) "$kcalText / ${food.servingUnit}" else kcalText
}
