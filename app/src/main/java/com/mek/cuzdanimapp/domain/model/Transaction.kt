package com.mek.cuzdanimapp.domain.model

data class Transaction(
    val id: Long,
    val type: String,
    val category: String,
    val amount: Double,
    val description: String?,
    val transactionDate: String
)