package com.mek.cuzdanimapp.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.mek.cuzdanimapp.presentation.auth.login.LoginScreen
import com.mek.cuzdanimapp.presentation.auth.login.LoginViewModel
import com.mek.cuzdanimapp.presentation.auth.register.RegisterScreen
import com.mek.cuzdanimapp.presentation.auth.register.RegisterViewModel
import com.mek.cuzdanimapp.presentation.budget.BudgetScreen
import com.mek.cuzdanimapp.presentation.dashboard.DashboardScreen
import com.mek.cuzdanimapp.presentation.main.MainScreen
import com.mek.cuzdanimapp.presentation.profile.ProfileScreen
import com.mek.cuzdanimapp.presentation.splash.SplashScreen
import com.mek.cuzdanimapp.presentation.splash.SplashViewModel
import com.mek.cuzdanimapp.presentation.transaction.TransactionsScreen

@Composable
fun AppNavGraph(
    onNavStateReady: (NavigationState) -> Unit
) {
    val navState = rememberNavigationState(
        startRoute = SplashRoute,
        topLevelRoutes = setOf(
            SplashRoute,
            LoginRoute,
            RegisterRoute,
            DashboardRoute,
            TransactionsRoute,
            BudgetRoute,
            ProfileRoute
        )
    )

    LaunchedEffect(navState) {
        onNavStateReady(navState)
    }

    val navigator = remember(navState) { Navigator(navState) }

    val entries = navState.toEntries { route ->
        when (route) {
            is SplashRoute -> NavEntry(route) {
                val viewModel: SplashViewModel = viewModel()
                SplashScreen(
                    onNavigateToDashboard = { navigator.replaceAll(DashboardRoute) },
                    onNavigateToLogin = { navigator.replaceAll(LoginRoute) },
                    viewModel = viewModel
                )
            }

            is LoginRoute -> NavEntry(route) {
                val viewModel: LoginViewModel = viewModel()
                LoginScreen(
                    onNavigateToDashboard = { navigator.replaceAll(DashboardRoute) },
                    onNavigateToRegister = { navigator.navigate(RegisterRoute) },
                    viewModel = viewModel
                )
            }

            is RegisterRoute -> NavEntry(route) {
                val viewModel: RegisterViewModel = viewModel()
                RegisterScreen(
                    onNavigateToDashboard = { navigator.replaceAll(DashboardRoute) },
                    onNavigateToLogin = { navigator.goBack() },
                    viewModel = viewModel
                )
            }

            is DashboardRoute -> NavEntry(route) {
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) }
                ) {
                    DashboardScreen()
                }
            }

            is TransactionsRoute -> NavEntry(route) {
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) }
                ) {
                    TransactionsScreen()
                }
            }

            is BudgetRoute -> NavEntry(route) {
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) }
                ) {
                    BudgetScreen()
                }
            }

            is ProfileRoute -> NavEntry(route) {
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) }
                ) {
                    ProfileScreen()
                }
            }

            else -> NavEntry(route) { Text("Bilinmeyen ekran") }
        }
    }

    NavDisplay(
        entries = entries,
        onBack = { navigator.goBack() }
    )
}