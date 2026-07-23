package com.example.healthtracker.data.local.database

import androidx.room.TypeConverter
import com.example.healthtracker.domain.model.Gender
import com.example.healthtracker.domain.model.Goal
import com.example.healthtracker.domain.model.MealType
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

    @TypeConverter
    fun genderToString(gender: Gender?): String? = gender?.name

    @TypeConverter
    fun stringToGender(value: String?): Gender? = value?.let(Gender::valueOf)

    @TypeConverter
    fun goalToString(goal: Goal?): String? = goal?.name

    @TypeConverter
    fun stringToGoal(value: String?): Goal? = value?.let(Goal::valueOf)

    @TypeConverter
    fun mealTypeToString(mealType: MealType?): String? = mealType?.name

    @TypeConverter
    fun stringToMealType(value: String?): MealType? = value?.let(MealType::valueOf)
}
