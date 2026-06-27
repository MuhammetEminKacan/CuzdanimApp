package com.mek.cuzdanimapp.domain.usecase.dashboard

import com.mek.cuzdanimapp.domain.model.DashboardData
import com.mek.cuzdanimapp.domain.repository.DashboardRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class GetDashboardSummaryUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(): Resource<DashboardData> {
        return repository.getSummary()
    }
}