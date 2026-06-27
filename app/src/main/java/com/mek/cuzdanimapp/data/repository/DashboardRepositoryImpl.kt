package com.mek.cuzdanimapp.data.repository

import com.mek.cuzdanimapp.data.mapper.toDomain
import com.mek.cuzdanimapp.data.remote.DashboardApi
import com.mek.cuzdanimapp.domain.model.DashboardData
import com.mek.cuzdanimapp.domain.repository.DashboardRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val api: DashboardApi
) : DashboardRepository {

    override suspend fun getSummary(): Resource<DashboardData> {
        return try {
            Resource.Success(api.getSummary().toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }
}