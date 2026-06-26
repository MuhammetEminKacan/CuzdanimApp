package com.mek.cuzdanimapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mek.cuzdanimapp.data.local.TokenManager
import com.mek.cuzdanimapp.presentation.navigation.AppNavGraph
import com.mek.cuzdanimapp.presentation.navigation.LoginRoute
import com.mek.cuzdanimapp.presentation.navigation.NavigationState
import com.mek.cuzdanimapp.ui.theme.CuzdanimAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuzdanimAppTheme {
                val navState = remember { mutableStateOf<NavigationState?>(null) }

                LaunchedEffect(Unit) {
                    tokenManager.logoutEvent.collect {
                        navState.value?.let { state ->
                            state.backStacks.values.forEach { it.clear() }
                            state.topLevelRoute = LoginRoute
                        }
                    }
                }

                AppNavGraph(
                    onNavStateReady = { navState.value = it }
                )
            }
        }
    }
}