package com.mek.cuzdanimapp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object LoginRoute : NavKey

@Serializable data object SplashRoute : NavKey

@Serializable data object RegisterRoute : NavKey
@Serializable data object DashboardRoute : NavKey

@Serializable data object TransactionsRoute : NavKey
@Serializable data object BudgetRoute : NavKey
@Serializable data object ProfileRoute : NavKey

@Serializable data object RecurringRoute : NavKey