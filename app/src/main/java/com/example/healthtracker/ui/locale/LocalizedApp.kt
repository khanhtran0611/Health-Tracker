package com.example.healthtracker.ui.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.healthtracker.domain.model.Language

/** Cung cấp lại LocalContext/LocalConfiguration đã đổi sang [language] cho toàn bộ [content] bên dưới — mọi stringResource() trong cây này đọc đúng ngôn ngữ đã chọn. */
@Composable
fun LocalizedApp(language: Language, content: @Composable () -> Unit) {
    val baseContext = LocalContext.current
    val localizedContext = remember(language) {
        createLocalizedContext(baseContext, language)
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedContext.resources.configuration,
    ) { content() }
}
