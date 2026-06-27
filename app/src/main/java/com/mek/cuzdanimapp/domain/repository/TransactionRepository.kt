package com.mek.cuzdanimapp.domain.repository

import com.mek.cuzdanimapp.domain.model.Transaction
import com.mek.cuzdanimapp.util.Resource

interface TransactionRepository {
    suspend fun getAllTransactions(): Resource<List<Transaction>>
    suspend fun createTransaction(
        type: String,
        category: String,
        amount: Double,
        description: String?,
        transactionDate: String
    ): Resource<Transaction>
    suspend fun updateTransaction(
        id: Long,
        type: String,
        category: String,
        amount: Double,
        description: String?,
        transactionDate: String
    ): Resource<Transaction>
    suspend fun deleteTransaction(id: Long): Resource<Unit>
}