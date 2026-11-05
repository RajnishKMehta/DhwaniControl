package io.github.rajnishkmehta.dhwanicontrol.core.locale

import android.content.Context
import android.app.LocaleConfig
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import org.xmlpull.v1.XmlPullParser
import java.util.Locale

object LocaleManager {

    /**
     * Retrieves the supported locales for the application.
     * With AGP generateLocaleConfig = true, it automatically generates
     * _generated_res_locale_config.xml at build time based on res/values-* folders.
     */
    fun getSupportedLocales(context: Context): List<Locale> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeConfig = LocaleConfig(context)
            if (localeConfig.status == LocaleConfig.STATUS_SUCCESS) {
                val supportedLocales = localeConfig.supportedLocales
                if (supportedLocales != null) {
                    val locales = mutableListOf<Locale>()
                    for (i in 0 until supportedLocales.size()) {
                        locales.add(supportedLocales.get(i))
                    }
                    return locales.distinctBy { it.toLanguageTag() }
                }
            }
        }

        val locales = mutableListOf<Locale>()
        try {
            val resId = context.resources.getIdentifier("_generated_res_locale_config", "xml", context.packageName)
            if (resId != 0) {
                val parser = context.resources.getXml(resId)
                var eventType = parser.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG && parser.name == "locale") {
                        val lang = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "name")
                        if (lang != null) {
                            locales.add(Locale.forLanguageTag(lang))
                        }
                    }
                    eventType = parser.next()
                }
            }
        } catch (e: Exception) {
            // Error occurred
        }
        return locales.distinctBy { it.toLanguageTag() }
    }

    fun getCurrentLocale(context: Context): Locale {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        if (!appLocales.isEmpty) {
            return appLocales[0] ?: Locale.getDefault()
        }
        return context.resources.configuration.locales[0] ?: Locale.getDefault()
    }

    fun setLocale(locale: Locale) {
        val localeList = LocaleListCompat.create(locale)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    fun resetLocale() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
    }

    fun getDisplayName(locale: Locale): String {
        return locale.getDisplayName(locale)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    }
}
