package com.mek.cuzdanimapp.presentation.dashboard

sealed class DashboardEffect {
    data class ShowError(val message: String) : DashboardEffect()
}