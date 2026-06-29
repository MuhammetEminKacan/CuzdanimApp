package com.mek.cuzdanimapp.domain.usecase.budget

import com.mek.cuzdanimapp.domain.model.Budget
import com.mek.cuzdanimapp.domain.repository.BudgetRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class UpdateBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(
        id: Long,
        category: String,
        monthlyLimit: Double
    ): Resource<Budget> = repository.updateBudget(id, category, monthlyLimit)
}