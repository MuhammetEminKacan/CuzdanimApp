package com.mek.cuzdanimapp

import android.app.Application
import com.mek.cuzdanimapp.data.local.LanguagePreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CuzdanimApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Bu sürümden önce seçilmiş dil tercihi varsa sisteme (LocaleManager)
        // taşınır; zaten uygulanmışsa bir şey yapmaz.
        LanguagePreferences.syncOnStartup(this)
    }
}
