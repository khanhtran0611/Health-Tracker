package com.example.healthtracker.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.theme.HealthTrackerTheme

@Composable
fun LoadingOverlay(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CircularProgressIndicator(color = Color.White)
            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingOverlayPreview() {
    HealthTrackerTheme {
        LoadingOverlay(textRes = R.string.text_loading)
    }
}
