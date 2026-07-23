package com.example.healthtracker.ui.component.entryform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.theme.appShapes
import com.example.healthtracker.ui.theme.borderWidths
import com.example.healthtracker.ui.theme.sizing
import com.example.healthtracker.ui.theme.spacing

@Composable
fun EntryFormSaveBar(
    onSave: () -> Unit,
    enabled: Boolean,
    isSaving: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        Button(
            onClick = onSave,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.lg)
                .height(MaterialTheme.sizing.buttonHeight),
            shape = MaterialTheme.appShapes.medium,
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(MaterialTheme.sizing.iconMedium),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = MaterialTheme.borderWidths.borderThick,
                )
            } else {
                Text(stringResource(R.string.action_save_and_use))
            }
        }
    }
}
