package com.mek.cuzdanimapp.domain.usecase.transaction

import com.mek.cuzdanimapp.domain.repository.TransactionRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Long): Resource<Unit> {
        return repository.deleteTransaction(id)
    }
}