package com.mek.cuzdanimapp.data.remote

import com.mek.cuzdanimapp.data.remote.dto.transaction.CreateTransactionRequestDto
import com.mek.cuzdanimapp.data.remote.dto.transaction.TransactionDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TransactionApi {

    @GET("transactions/all")
    suspend fun getAllTransactions(): List<TransactionDto>

    @POST("transactions/create")
    suspend fun createTransaction(
        @Body request: CreateTransactionRequestDto
    ): TransactionDto

    @PUT("transactions/update/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Long,
        @Body request: CreateTransactionRequestDto
    ): TransactionDto

    @DELETE("transactions/delete/{id}")
    suspend fun deleteTransaction(
        @Path("id") id: Long
    )
}