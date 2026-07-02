package com.mek.cuzdanimapp.presentation.dashboard

sealed class DashboardEvent {
    data object LoadDashboard : DashboardEvent()
    data object Refresh : DashboardEvent()
    data object OnSeeAllPaymentsClicked : DashboardEvent()
    data object OnSeeAllTransactionsClicked : DashboardEvent()
}