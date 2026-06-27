package com.mek.cuzdanimapp.domain.usecase.transaction

import com.mek.cuzdanimapp.domain.model.Transaction
import com.mek.cuzdanimapp.domain.repository.TransactionRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): Resource<List<Transaction>> {
        return repository.getAllTransactions()
    }
}