package com.mek.cuzdanimapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

data class BottomNavItem(
    val route: NavKey,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(DashboardRoute, "Ana Sayfa", Icons.Default.Dashboard),
    BottomNavItem(TransactionsRoute, "İşlemler", Icons.Default.Receipt),
    BottomNavItem(RecurringRoute, "Düzenli", Icons.Default.Repeat),
    BottomNavItem(BudgetRoute, "Bütçe", Icons.Default.PieChart),
    BottomNavItem(ProfileRoute, "Profil", Icons.Default.AccountCircle)
)