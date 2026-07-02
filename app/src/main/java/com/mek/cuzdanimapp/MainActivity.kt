package com.mek.cuzdanimapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mek.cuzdanimapp.data.local.LanguagePreferences
import com.mek.cuzdanimapp.data.local.ThemePreferences
import com.mek.cuzdanimapp.data.local.TokenManager
import com.mek.cuzdanimapp.presentation.navigation.AppNavGraph
import com.mek.cuzdanimapp.presentation.navigation.NavigationState
import com.mek.cuzdanimapp.ui.theme.CuzdanimAppTheme
import com.mek.cuzdanimapp.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var themePreferences: ThemePreferences

    @Inject
    lateinit var languagePreferences: LanguagePreferences

    override fun attachBaseContext(newBase: Context) {
        val languageCode = LocaleHelper.getSavedLanguageBlocking(newBase)
        val context = LocaleHelper.wrapContext(newBase, languageCode)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by themePreferences.isDarkMode.collectAsState(initial = false)

            CuzdanimAppTheme(darkTheme = isDarkMode) {
                val navState = remember { mutableStateOf<NavigationState?>(null) }

                LaunchedEffect(Unit) {
                    tokenManager.logoutEvent.collect {
                        kotlinx.coroutines.delay(300)
                        recreate()
                    }
                }

                AppNavGraph(
                    onNavStateReady = { navState.value = it }
                )
            }
        }
    }
}