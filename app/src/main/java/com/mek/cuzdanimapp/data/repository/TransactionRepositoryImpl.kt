package com.mek.cuzdanimapp.data.repository

import com.mek.cuzdanimapp.data.mapper.toDomain
import com.mek.cuzdanimapp.data.remote.TransactionApi
import com.mek.cuzdanimapp.data.remote.dto.transaction.CreateTransactionRequestDto
import com.mek.cuzdanimapp.domain.model.Transaction
import com.mek.cuzdanimapp.domain.repository.TransactionRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi
) : TransactionRepository {

    override suspend fun getAllTransactions(): Resource<List<Transaction>> {
        return try {
            Resource.Success(api.getAllTransactions().map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun createTransaction(
        type: String,
        category: String,
        amount: Double,
        description: String?,
        transactionDate: String
    ): Resource<Transaction> {
        return try {
            val response = api.createTransaction(
                CreateTransactionRequestDto(type, category, amount, description, transactionDate)
            )
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun updateTransaction(
        id: Long,
        type: String,
        category: String,
        amount: Double,
        description: String?,
        transactionDate: String
    ): Resource<Transaction> {
        return try {
            val response = api.updateTransaction(
                id,
                CreateTransactionRequestDto(type, category, amount, description, transactionDate)
            )
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun deleteTransaction(id: Long): Resource<Unit> {
        return try {
            api.deleteTransaction(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }
}