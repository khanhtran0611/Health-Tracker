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
    // LocalContext.current trả về Context chứa thông tin của Activity hiện tại
    val baseContext = LocalContext.current

    val localizedContext = remember(language) {
        createLocalizedContext(baseContext, language)
    }
    CompositionLocalProvider(
        // Từ giờ trong content(), ai gọi LocalContext.current sẽ nhận localizedContext (Context đã đổi locale)
        // — thay vì Context gốc mặc định
        LocalContext provides localizedContext,
        // dòng bổ sung, mang tính "phòng hờ"
        // Nếu chỉ cung cấp LocalContext mà quên LocalConfiguration,
        // có rủi ro 1 số Composable không nhận ra Configuration đã đổi
        // (vì chúng đang dựa vào LocalConfiguration, không phải LocalContext),
        // dẫn tới string không được vẽ lại đúng lúc
        LocalConfiguration provides localizedContext.resources.configuration,
    ) { content() }
}
