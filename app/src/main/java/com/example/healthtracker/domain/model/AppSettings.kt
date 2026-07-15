package com.example.healthtracker.domain.model

/** Ngôn ngữ hiển thị. */
enum class Language {
    VI,
    EN,
}

/** Cỡ chữ toàn app. */
enum class FontSize {
    SMALL,
    MEDIUM,
    LARGE,
}

/**
 * Cài đặt app, lưu qua DataStore (không lưu Room). Theme sẽ thêm sau.
 */
data class AppSettings(
    val language: Language = Language.VI,
    val fontSize: FontSize = FontSize.MEDIUM,
)
