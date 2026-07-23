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

private class LocalizedContextWrapper(
    base: Context,
    private val localizedResources: Resources,
) : ContextWrapper(base) {
    override fun getResources(): Resources = localizedResources
}

fun createLocalizedContext(baseContext: Context, language: Language): Context {
    val locale = localeOf(language)
    Locale.setDefault(locale)
    val config = Configuration(baseContext.resources.configuration).apply {
        setLocale(locale)
    }

    val localizedResources = baseContext.createConfigurationContext(config).resources

    return LocalizedContextWrapper(baseContext, localizedResources)
}
