package com.mek.cuzdanimapp.data.remote.dto.budget

data class BudgetDto(
    val id: Long,
    val category: String,
    val monthlyLimit: Double,
    val spentAmount: Double,
    val remainingAmount: Double,
    val usagePercentage: Double
)
