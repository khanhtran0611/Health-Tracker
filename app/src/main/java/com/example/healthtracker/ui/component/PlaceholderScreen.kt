package com.example.healthtracker.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Màn tạm cho khung điều hướng: hiện tên màn + các nút đi tiếp. Sẽ được thay bằng
 * màn thật (kèm ViewModel) sau. Mỗi action là cặp (nhãn nút, hành động điều hướng).
 */
@Composable
fun PlaceholderScreen(
    title: String,
    modifier: Modifier = Modifier,
    actions: List<Pair<String, () -> Unit>> = emptyList(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        actions.forEach { (label, onClick) ->
            Button(onClick = onClick, modifier = Modifier.widthIn(min = 220.dp)) {
                Text(label)
            }
        }
    }
}
