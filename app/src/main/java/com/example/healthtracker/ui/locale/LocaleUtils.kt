package com.example.healthtracker.ui.locale

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import com.example.healthtracker.domain.model.Language
import java.util.Locale

fun localeOf(language: Language): Locale = when (language) {
    Language.VI -> Locale.forLanguageTag("vi")
    Language.EN -> Locale.forLanguageTag("en")
}

/**
 * Context có resources đọc theo [language], NHƯNG vẫn là [ContextWrapper] của
 * [base] (không phải Context trần từ `createConfigurationContext`) — giữ nguyên
 * chuỗi baseContext trỏ về Activity, để `hiltViewModel()` unwrap tìm ra Activity
 * vẫn hoạt động (dùng `createConfigurationContext` trực tiếp làm LocalContext
 * sẽ cắt đứt chuỗi này -> crash "Expected an activity context").
 */
private class LocalizedContextWrapper(
    base: Context,
    private val localizedResources: Resources,
) : ContextWrapper(base) {
    override fun getResources(): Resources = localizedResources
}

/** Bọc [baseContext] để resources đọc theo [language] — dùng đổi ngôn ngữ runtime, không cần recreate Activity. */
fun createLocalizedContext(baseContext: Context, language: Language): Context {
    val locale = localeOf(language)
    Locale.setDefault(locale)
    val config = Configuration(baseContext.resources.configuration).apply {
        setLocale(locale)
    }
    // createConfigurationContext là 1 hàm có sẵn của mọi context.
    // tham số là 1 cái configuration , nó sẽ tạo và trả về 1 cái context hoàn toàn mới,
    // và context này sẽ dùng đúng configuration đã đưa vào.
    val localizedResources = baseContext.createConfigurationContext(config).resources

    // Tuy nhiên, cái context này có 1 nhược điểm, đó là không có bất kỳ liên hệ nào với Activity gốc
    // Khi hilt tìm activity trong cái context, nó ko tìm thấy cái activity nào cả.  => báo lỗi và crash hệ thống
    // Vì thế tạo ra class LocalizedContextWrapper
    // => giữ nguyên context gốc, trong chỉ thay đổi phần resources trả về (chứa config mới)
    return LocalizedContextWrapper(baseContext, localizedResources)
}
