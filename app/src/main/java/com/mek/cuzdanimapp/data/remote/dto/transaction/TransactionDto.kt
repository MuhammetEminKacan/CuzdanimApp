package com.mek.cuzdanimapp.data.remote.dto.transaction

data class TransactionDto(
    val id: Long,
    val type: String,
    val category: String,
    val amount: Double,
    val description: String?,
    val transactionDate: String
)
