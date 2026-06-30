package com.mek.cuzdanimapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.languageDataStore by preferencesDataStore(name = "language_prefs")

@Singleton
class LanguagePreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val LANGUAGE_KEY = stringPreferencesKey("language_code")

    val languageCode: Flow<String?> = context.languageDataStore.data
        .map { preferences -> preferences[LANGUAGE_KEY] }

    suspend fun setLanguage(code: String?) {
        context.languageDataStore.edit { preferences ->
            if (code == null) {
                preferences.remove(LANGUAGE_KEY)
            } else {
                preferences[LANGUAGE_KEY] = code
            }
        }
    }
}