package com.example.healthtracker.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.healthtracker.R

/**
 * Loại lỗi của 1 field trong form (dùng chung cho mọi form: ProfileForm, Enter
 * Food Manually...). Dùng enum (không phải String) vì hàm validate là Kotlin
 * thuần — chữ lỗi hiển thị (strings.xml) do Composable tự map enum này sang
 * string lúc render.
 */
enum class FieldError {
    REQUIRED,
    INVALID_NUMBER,
    MUST_BE_POSITIVE,
    WEIGHT_OUT_OF_RANGE,
    HEIGHT_OUT_OF_RANGE,
    AGE_OUT_OF_RANGE,
}

/** Map FieldError sang chữ hiển thị (strings.xml) — dùng chung cho mọi form. */
@Composable
fun fieldErrorText(error: FieldError?): String? = when (error) {
    null -> null
    FieldError.REQUIRED -> stringResource(R.string.error_field_required)
    FieldError.INVALID_NUMBER -> stringResource(R.string.error_invalid_number)
    FieldError.MUST_BE_POSITIVE -> stringResource(R.string.error_must_be_positive)
    FieldError.WEIGHT_OUT_OF_RANGE -> stringResource(R.string.error_weight_out_of_range)
    FieldError.HEIGHT_OUT_OF_RANGE -> stringResource(R.string.error_height_out_of_range)
    FieldError.AGE_OUT_OF_RANGE -> stringResource(R.string.error_age_out_of_range)
}
