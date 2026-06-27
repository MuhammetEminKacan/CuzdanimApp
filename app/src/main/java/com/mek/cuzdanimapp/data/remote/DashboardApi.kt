package com.mek.cuzdanimapp.data.remote

import com.mek.cuzdanimapp.data.remote.dto.dashboard.DashboardResponseDto
import retrofit2.http.GET

interface DashboardApi {
    @GET("dashboard/summary")
    suspend fun getSummary(): DashboardResponseDto
}