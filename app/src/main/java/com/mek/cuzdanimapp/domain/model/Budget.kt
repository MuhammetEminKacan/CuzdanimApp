package com.mek.cuzdanimapp.domain.model

data class Budget(
    val id: Long,
    val category: String,
    val monthlyLimit: Double,
    val spentAmount: Double,
    val remainingAmount: Double,
    val usagePercentage: Double
)