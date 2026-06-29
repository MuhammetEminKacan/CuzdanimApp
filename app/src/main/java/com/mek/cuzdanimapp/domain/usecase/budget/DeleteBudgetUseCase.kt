package com.mek.cuzdanimapp.domain.usecase.budget

import com.mek.cuzdanimapp.domain.repository.BudgetRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(id: Long): Resource<Unit> = repository.deleteBudget(id)
}