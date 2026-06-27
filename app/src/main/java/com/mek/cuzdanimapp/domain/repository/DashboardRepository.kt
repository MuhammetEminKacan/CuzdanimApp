package com.mek.cuzdanimapp.domain.repository

import com.mek.cuzdanimapp.domain.model.DashboardData
import com.mek.cuzdanimapp.util.Resource

interface DashboardRepository {
    suspend fun getSummary(): Resource<DashboardData>
}