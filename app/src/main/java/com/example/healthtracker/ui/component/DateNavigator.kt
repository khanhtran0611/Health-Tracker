package com.example.healthtracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Thanh chuyển ngày: "< Hôm nay, 14 tháng 7 >" ở giữa, icon lịch ghim mép phải.
 * Dùng Box + Alignment (không phải Row + SpaceBetween) để cụm giữa LUÔN ở chính
 * giữa dù icon lịch có đổi kích thước — Box định vị từng con theo toàn bộ chiều
 * rộng của chính nó, không phụ thuộc con bên cạnh.
 *
 * Dùng chung cho Meal Diary lẫn Activity Diary.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateNavigator(
    selectedDate: LocalDate,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateText = formatDiaryDate(selectedDate)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(999.dp))
            .padding(vertical = 4.dp, horizontal = 4.dp),
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onPreviousDay) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = stringResource(R.string.action_previous_day))
            }
            Text(dateText, style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = onNextDay) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = stringResource(R.string.action_next_day))
            }
        }

        IconButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = stringResource(R.string.action_pick_date),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }

    if (showDatePicker) {
        // DatePicker cần 1 object "state" riêng để:
        // (a) biết ngày nào đang được tô đậm lúc mở lên,
        // (b) tự cập nhật khi người dùng bấm chọn ngày khác trong lưới
        // datePickerState chính là cái object state đó.
        val datePickerState = rememberDatePickerState(
            // không làm việc với LocalDate — nó chỉ hiểu số mili-giây kể từ mốc 1/1/1970 (epoch millis),
            // giống hệt kiểu System.currentTimeMillis(). Nên phải "dịch" LocalDate sang con số đó
            initialSelectedDateMillis = selectedDate
                 .atStartOfDay(ZoneOffset.UTC) // gắn thêm giờ 00:00:00, ở múi giờ UTC
                 .toInstant() // đổi thành 1 "thời điểm" tuyệt đối
                 .toEpochMilli(), // đổi thành số milli-giây
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        // ↑ dịch ngược: epoch millis -> Instant -> gắn UTC -> lấy ra LocalDate
                        onDateSelected(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.action_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
