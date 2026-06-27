package com.mek.cuzdanimapp.data.remote.dto.transaction

data class CreateTransactionRequestDto(
    val type: String,
    val category: String,
    val amount: Double,
    val description: String?,
    val transactionDate: String
)
