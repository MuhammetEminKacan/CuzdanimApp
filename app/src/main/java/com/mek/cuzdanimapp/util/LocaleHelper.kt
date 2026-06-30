package com.mek.cuzdanimapp.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mek.cuzdanimapp.data.local.languageDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

private val LANGUAGE_KEY = stringPreferencesKey("language_code")

object LocaleHelper {

    fun getSavedLanguageBlocking(context: Context): String? {
        return runBlocking {
            context.languageDataStore.data.first()[LANGUAGE_KEY]
        }
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