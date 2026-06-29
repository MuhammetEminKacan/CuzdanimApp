package com.mek.cuzdanimapp.domain.repository

import com.mek.cuzdanimapp.domain.model.Budget
import com.mek.cuzdanimapp.util.Resource

interface BudgetRepository {
    suspend fun getAllBudgets(): Resource<List<Budget>>
    suspend fun createBudget(category: String, monthlyLimit: Double): Resource<Budget>
    suspend fun updateBudget(id: Long, category: String, monthlyLimit: Double): Resource<Budget>
    suspend fun deleteBudget(id: Long): Resource<Unit>
}