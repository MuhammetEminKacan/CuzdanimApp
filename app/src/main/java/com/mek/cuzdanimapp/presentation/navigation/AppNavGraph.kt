package com.mek.cuzdanimapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.mek.cuzdanimapp.presentation.auth.login.LoginEvent
import com.mek.cuzdanimapp.presentation.auth.login.LoginScreen
import com.mek.cuzdanimapp.presentation.auth.login.LoginViewModel
import com.mek.cuzdanimapp.presentation.auth.register.RegisterEvent
import com.mek.cuzdanimapp.presentation.auth.register.RegisterScreen
import com.mek.cuzdanimapp.presentation.auth.register.RegisterViewModel
import com.mek.cuzdanimapp.presentation.budget.BudgetScreen
import com.mek.cuzdanimapp.presentation.budget.BudgetViewModel
import com.mek.cuzdanimapp.presentation.dashboard.DashboardScreen
import com.mek.cuzdanimapp.presentation.dashboard.DashboardViewModel
import com.mek.cuzdanimapp.presentation.main.AddTransactionBottomSheet
import com.mek.cuzdanimapp.presentation.main.AddTransactionSheetEffect
import com.mek.cuzdanimapp.presentation.main.AddTransactionSheetEvent
import com.mek.cuzdanimapp.presentation.main.AddTransactionViewModel
import com.mek.cuzdanimapp.presentation.main.MainScreen
import com.mek.cuzdanimapp.presentation.profile.ProfileScreen
import com.mek.cuzdanimapp.presentation.profile.ProfileViewModel
import com.mek.cuzdanimapp.presentation.recurring.RecurringPaymentViewModel
import com.mek.cuzdanimapp.presentation.recurring.RecurringPaymentsScreen
import com.mek.cuzdanimapp.presentation.splash.SplashScreen
import com.mek.cuzdanimapp.presentation.splash.SplashViewModel
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

    var showVerificationMessage by remember { mutableStateOf(false) }

    LaunchedEffect(navState) { onNavStateReady(navState) }

    val navigator = remember(navState) { Navigator(navState) }

    // Global ViewModel — tüm ekranlardan erişilebilir
    val addTransactionViewModel: AddTransactionViewModel = viewModel()

    val entries = navState.toEntries { route ->
        when (route) {
            is SplashRoute -> NavEntry(route) {
                val splashViewModel: SplashViewModel = viewModel()
                SplashScreen(
                    onNavigateToDashboard = {
                        navState.backStacks[SplashRoute]?.clear()
                        navigator.navigate(DashboardRoute)
                    },
                    onNavigateToLogin = {
                        navState.backStacks[SplashRoute]?.clear()
                        navigator.navigate(LoginRoute)
                    },
                    viewModel = splashViewModel
                )
            }
            is LoginRoute -> NavEntry(route) {
                val loginViewModel: LoginViewModel = viewModel()
                LaunchedEffect(Unit) {
                    loginViewModel.onEvent(LoginEvent.ClearState)
                }
                LoginScreen(
                    onNavigateToDashboard = { navigator.replaceAll(DashboardRoute) },
                    onNavigateToRegister = { navigator.navigate(RegisterRoute) },
                    viewModel = loginViewModel
                )
            }

            is RegisterRoute -> NavEntry(route) {
                val viewModel: RegisterViewModel = viewModel()

                LaunchedEffect(Unit) {
                    viewModel.onEvent(RegisterEvent.ClearState)
                }

                RegisterScreen(
                    onNavigateToDashboard = { navigator.replaceAll(DashboardRoute) },
                    onNavigateToLogin = { navigator.replaceAll(LoginRoute) },
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
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigate = { route -> navigator.navigate(route) }
                    )
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
                    TransactionsScreen(viewModel = viewModel)
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

            is BudgetRoute -> NavEntry(route) {
                val viewModel: BudgetViewModel = viewModel()
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) },
                    onAddTransaction = {
                        addTransactionViewModel.onEvent(AddTransactionSheetEvent.Show)
                    }
                ) {
                    BudgetScreen(viewModel = viewModel)
                }
            }

            is ProfileRoute -> NavEntry(route) {
                val viewModel: ProfileViewModel = viewModel()
                MainScreen(
                    currentRoute = navState.topLevelRoute,
                    onNavigate = { navigator.navigate(it) },
                    onAddTransaction = {
                        addTransactionViewModel.onEvent(AddTransactionSheetEvent.Show)
                    }
                ) {
                    ProfileScreen(
                        viewModel = viewModel,
                        onLoggedOut = { navigator.replaceAll(LoginRoute) }
                    )
                }
            }

            else -> NavEntry(route) { }
        }
    }

    AddTransactionBottomSheet(
        viewModel = addTransactionViewModel,
        onTransactionAdded = { }
    )

    NavDisplay(
        entries = entries,
        onBack = { navigator.goBack() }
    )
}