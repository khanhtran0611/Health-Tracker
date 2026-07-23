package com.example.healthtracker.ui.component.formatting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.healthtracker.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun formatDiaryDate(date: LocalDate): String {
    val formatter = remember { DateTimeFormatter.ofPattern("d MMMM", Locale.getDefault()) }
    val formatted = date.format(formatter)
    return if (date == LocalDate.now()) {
        stringResource(R.string.date_today_prefix, formatted)
    } else {
        formatted
    }
}

@Composable
fun formatWeekdayShort(date: LocalDate): String {
    val formatter = remember { DateTimeFormatter.ofPattern("EEE", Locale.getDefault()) }
    return date.format(formatter)
}
