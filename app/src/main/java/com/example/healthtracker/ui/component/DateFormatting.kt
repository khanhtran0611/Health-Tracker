package com.example.healthtracker.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.healthtracker.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** "Hôm nay, 16 tháng 7" nếu date là hôm nay, ngược lại chỉ "16 tháng 7". */
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
