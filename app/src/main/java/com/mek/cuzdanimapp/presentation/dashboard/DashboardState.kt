package com.mek.cuzdanimapp.presentation.dashboard

import com.mek.cuzdanimapp.domain.model.DashboardData

data class DashboardState(
    val isLoading: Boolean = false,
    val dashboardData: DashboardData? = null
)