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
import com.mek.cuzdanimapp.presentation.dashboard.DashboardViewModel
import com.mek.cuzdanimapp.presentation.main.AddTransactionBottomSheet
import com.mek.cuzdanimapp.presentation.main.AddTransactionSheetEffect
import com.mek.cuzdanimapp.presentation.main.AddTransactionSheetEvent
import com.mek.cuzdanimapp.presentation.main.AddTransactionViewModel
import com.mek.cuzdanimapp.presentation.main.MainScreen
import com.mek.cuzdanimapp.presentation.profile.ProfileScreen
import com.mek.cuzdanimapp.presentation.recurring.RecurringPaymentViewModel
import com.mek.cuzdanimapp.presentation.recurring.RecurringPaymentsScreen
import com.mek.cuzdanimapp.presentation.splash.SplashScreen
import com.mek.cuzdanimapp.presentation.splash.SplashViewModel
import com.mek.cuzdanimapp.presentation.transaction.TransactionEvent
import com.mek.cuzdanimapp.presentation.transaction.TransactionViewModel
import com.mek.cuzdanimapp.presentation.transaction.TransactionsScreen

@Composable
fun AppNavGraph(
    onNavStateReady: (NavigationState) -> Unit
) {
    val navState = rememberNavigationState(
        startRoute = SplashRoute,
        topLevelRoutes = setOf(
            SplashRoute, LoginRoute, RegisterRoute,
            DashboardRoute, TransactionsRoute, RecurringRoute, BudgetRoute, ProfileRoute
        )
    )

    LaunchedEffect(navState) { onNavStateReady(navState) }

    val navigator = remember(navState) { Navigator(navState) }

    // Global ViewModel — tüm ekranlardan erişilebilir
    val addTransactionViewModel: AddTransactionViewModel = viewModel()

    // İşlem eklenince Dashboard'u yenile
    LaunchedEffect(Unit) {
        addTransactionViewModel.effect.collect { effect ->
            if (effect is AddTransactionSheetEffect.TransactionAdded) {
                // Dashboard açıksa yenile — ileride event bus ile yapılabilir
                // Şimdilik bottom sheet kapanması yeterli
            }
        }
    }

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
                val viewModel: DashboardViewModel = viewModel()
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) },
                    onAddTransaction = {
                        addTransactionViewModel.onEvent(AddTransactionSheetEvent.Show)
                    }
                ) {
                    DashboardScreen(viewModel = viewModel)
                }
            }
            is TransactionsRoute -> NavEntry(route) {
                val viewModel: TransactionViewModel = viewModel()
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) },
                    onAddTransaction = {
                        addTransactionViewModel.onEvent(AddTransactionSheetEvent.Show)
                    }
                ) {
                    TransactionsScreen(
                        viewModel = viewModel,
                        onTransactionAdded = {
                            viewModel.onEvent(TransactionEvent.Refresh)
                        }
                    )
                }
            }
            is BudgetRoute -> NavEntry(route) {
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) },
                    onAddTransaction = {
                        addTransactionViewModel.onEvent(AddTransactionSheetEvent.Show)
                    }
                ) {
                    BudgetScreen()
                }
            }
            is ProfileRoute -> NavEntry(route) {
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) },
                    onAddTransaction = {
                        addTransactionViewModel.onEvent(AddTransactionSheetEvent.Show)
                    }
                ) {
                    ProfileScreen()
                }
            }

            is RecurringRoute -> NavEntry(route) {
                val viewModel: RecurringPaymentViewModel = viewModel()
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) },
                    onAddTransaction = {
                        addTransactionViewModel.onEvent(AddTransactionSheetEvent.Show)
                    }
                ) {
                    RecurringPaymentsScreen(viewModel = viewModel)
                }
            }
            else -> NavEntry(route) { Text("Bilinmeyen ekran") }
        }
    }

    // Global bottom sheet — tüm ekranların üzerinde
    AddTransactionBottomSheet(
        viewModel = addTransactionViewModel,
        onTransactionAdded = {
            // Dashboard'daysa yenile
        }
    )

    NavDisplay(
        entries = entries,
        onBack = { navigator.goBack() }
    )
}