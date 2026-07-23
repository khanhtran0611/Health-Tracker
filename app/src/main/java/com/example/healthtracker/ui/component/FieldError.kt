package com.example.healthtracker.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.healthtracker.R

enum class FieldError {
    REQUIRED,
    INVALID_NUMBER,
    MUST_BE_POSITIVE,
    WEIGHT_OUT_OF_RANGE,
    HEIGHT_OUT_OF_RANGE,
    AGE_OUT_OF_RANGE,
}

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
