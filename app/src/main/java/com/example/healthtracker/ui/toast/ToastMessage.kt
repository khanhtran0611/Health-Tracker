package com.example.healthtracker.ui.toast

import androidx.annotation.StringRes

enum class ToastType {
    SUCCESS, ERROR, INFO, WARNING
}

data class ToastMessage(
    @param:StringRes val textRes: Int,
    val type: ToastType = ToastType.INFO
)
