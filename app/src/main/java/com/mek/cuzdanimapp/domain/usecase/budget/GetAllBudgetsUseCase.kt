package com.mek.cuzdanimapp.domain.usecase.budget

import com.mek.cuzdanimapp.domain.model.Budget
import com.mek.cuzdanimapp.domain.repository.BudgetRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class GetAllBudgetsUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(): Resource<List<Budget>> = repository.getAllBudgets()
}