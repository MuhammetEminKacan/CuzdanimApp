package com.mek.cuzdanimapp.presentation.dashboard

import androidx.navigation3.runtime.NavKey

sealed class DashboardEffect {
    data class ShowError(val message: String) : DashboardEffect()
    data class NavigateTo(val route: NavKey) : DashboardEffect()
}