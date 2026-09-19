package com.mek.cuzdanimapp.data.local

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.languageDataStore by preferencesDataStore(name = "language_prefs")

@Singleton
class LanguagePreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val languageKey = stringPreferencesKey("language_code")

    val languageCode: Flow<String?> = context.languageDataStore.data
        .map { preferences -> preferences[languageKey] }

    suspend fun setLanguage(code: String?) {
        // Senkron kopya: Activity.attachBaseContext() suspend olamadığı için
        // uygulama açılışında dili DataStore yerine buradan hızlıca okuyoruz.
        context.getSharedPreferences(SYNC_PREFS_NAME, Context.MODE_PRIVATE).edit {
            if (code == null) remove(SYNC_KEY) else putString(SYNC_KEY, code)
        }
        context.languageDataStore.edit { preferences ->
            if (code == null) {
                preferences.remove(languageKey)
            } else {
                preferences[languageKey] = code
            }
        }
        // Asıl dil değişimi burada oluyor: Android 13+ tarafında sistemin
        // per-app language kaydını (LocaleManager) günceller, altındaki
        // sürümlerde AppCompat kendi deposunda tutar.
        applyToDelegate(code)
    }

    companion object {
        private const val SYNC_PREFS_NAME = "language_prefs_sync"
        private const val SYNC_KEY = "language_code"

        fun getSavedLanguageSync(context: Context): String? =
            context.getSharedPreferences(SYNC_PREFS_NAME, Context.MODE_PRIVATE)
                .getString(SYNC_KEY, null)

        /** AppCompat/LocaleManager tarafında ayarlı dil kodu (yoksa null). */
        fun getAppliedLanguage(): String? =
            AppCompatDelegate.getApplicationLocales()
                .takeIf { !it.isEmpty }
                ?.get(0)
                ?.language

        suspend fun applyToDelegate(code: String?) {
            // AppCompatDelegate yalnızca ana thread'den çağrılmalı.
            withContext(Dispatchers.Main) {
                AppCompatDelegate.setApplicationLocales(
                    if (code == null) LocaleListCompat.getEmptyLocaleList()
                    else LocaleListCompat.forLanguageTags(code)
                )
            }
        }

        /**
         * Uygulama açılışında bir kez çağrılır: daha önce kaydedilmiş dil
         * tercihi varken sistem tarafında henüz uygulanmamışsa (ör. bu
         * sürüme güncellemeden önce seçilmiş dil) onu sisteme taşır.
         */
        fun syncOnStartup(context: Context) {
            val saved = getSavedLanguageSync(context) ?: return
            if (getAppliedLanguage() == saved) return
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(saved))
        }
    }
}
