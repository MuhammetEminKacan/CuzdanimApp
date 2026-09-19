package com.mek.cuzdanimapp.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.mek.cuzdanimapp.data.local.LanguagePreferences
import java.util.Locale

object LocaleHelper {

    /**
     * Aktif dil kodu. Android 13+'ta sistemin per-app language kaydı (LocaleManager)
     * asıl kaynaktır; daha eski sürümlerde AppCompat'ın deposu boş kalabildiği için
     * senkron SharedPreferences kopyasına düşeriz.
     */
    fun getSavedLanguage(context: Context): String? {
        return LanguagePreferences.getAppliedLanguage()
            ?: LanguagePreferences.getSavedLanguageSync(context)
    }

    fun wrapContext(context: Context, languageCode: String?): Context {
        val locale = if (languageCode != null) {
            Locale(languageCode)
        } else {
            getSystemLocale()
        }

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    private fun getSystemLocale(): Locale {
        val systemConfig = Resources.getSystem().configuration
        return systemConfig.locales.get(0)
    }
}
