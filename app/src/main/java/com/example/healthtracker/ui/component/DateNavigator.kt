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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.component.formatting.formatDiaryDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

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

        val localContext = LocalContext.current
        val localConfiguration = LocalConfiguration.current

        val datePickerState = rememberDatePickerState(

            initialSelectedDateMillis = selectedDate
                 .atStartOfDay(ZoneOffset.UTC)
                 .toInstant()
                 .toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {

                            onDateSelected(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                        }
                        showDatePicker = false
                    }) {
                        Text(stringResource(R.string.action_confirm))
                    }
                }
            },
            dismissButton = {
                CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.action_cancel))
                    }
                }
            },
        ) {
            CompositionLocalProvider(LocalContext provides localContext, LocalConfiguration provides localConfiguration) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
