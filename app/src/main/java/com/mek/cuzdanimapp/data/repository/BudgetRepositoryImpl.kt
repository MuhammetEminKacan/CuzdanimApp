package com.mek.cuzdanimapp.data.repository

import com.mek.cuzdanimapp.data.mapper.toDomain
import com.mek.cuzdanimapp.data.remote.BudgetApi
import com.mek.cuzdanimapp.data.remote.dto.budget.CreateBudgetRequestDto
import com.mek.cuzdanimapp.domain.model.Budget
import com.mek.cuzdanimapp.domain.repository.BudgetRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val api: BudgetApi
) : BudgetRepository {

    override suspend fun getAllBudgets(): Resource<List<Budget>> {
        return try {
            Resource.Success(api.getAllBudgets().map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun createBudget(
        category: String,
        monthlyLimit: Double
    ): Resource<Budget> {
        return try {
            Resource.Success(
                api.createBudget(CreateBudgetRequestDto(category, monthlyLimit)).toDomain()
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun updateBudget(
        id: Long,
        category: String,
        monthlyLimit: Double
    ): Resource<Budget> {
        return try {
            Resource.Success(
                api.updateBudget(id, CreateBudgetRequestDto(category, monthlyLimit)).toDomain()
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun deleteBudget(id: Long): Resource<Unit> {
        return try {
            api.deleteBudget(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }
}