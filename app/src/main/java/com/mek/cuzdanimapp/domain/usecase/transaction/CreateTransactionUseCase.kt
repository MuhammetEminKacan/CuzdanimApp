package com.mek.cuzdanimapp.domain.usecase.transaction

import com.mek.cuzdanimapp.domain.model.Transaction
import com.mek.cuzdanimapp.domain.repository.TransactionRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        type: String,
        category: String,
        amount: Double,
        description: String?,
        transactionDate: String
    ): Resource<Transaction> {
        return repository.createTransaction(type, category, amount, description, transactionDate)
    }
}
