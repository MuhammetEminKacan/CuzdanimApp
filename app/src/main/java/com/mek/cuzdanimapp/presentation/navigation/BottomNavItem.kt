package com.mek.cuzdanimapp.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.mek.cuzdanimapp.R

data class BottomNavItem(
    val route: NavKey,
    @StringRes val labelRes: Int, // String yerine String Resource ID tutuyoruz
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(DashboardRoute, R.string.nav_dashboard, Icons.Default.Dashboard),
    BottomNavItem(TransactionsRoute, R.string.nav_transactions, Icons.Default.Receipt),
    BottomNavItem(RecurringRoute, R.string.nav_recurring, Icons.Default.Repeat),
    BottomNavItem(BudgetRoute, R.string.nav_budget, Icons.Default.PieChart),
    BottomNavItem(ProfileRoute, R.string.nav_profile, Icons.Default.AccountCircle)
)