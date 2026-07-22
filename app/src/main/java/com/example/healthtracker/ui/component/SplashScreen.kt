package com.example.healthtracker.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.ui.theme.HealthTrackerTheme

/**
 * Màn splash — hiện lúc app vừa mở, thay cho spinner trơn trong lúc
 * [com.example.healthtracker.ui.navigation.AppStartViewModel] xác định đã có
 * hồ sơ user chưa (AppStartDestination.LOADING).
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_app_logo),
            contentDescription = null,
            modifier = Modifier.size(140.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    HealthTrackerTheme {
        SplashScreen()
    }
}
