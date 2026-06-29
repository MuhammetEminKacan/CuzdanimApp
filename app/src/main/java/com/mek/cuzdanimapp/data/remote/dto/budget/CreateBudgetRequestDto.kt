package com.mek.cuzdanimapp.data.remote.dto.budget

data class CreateBudgetRequestDto(
    val category: String,
    val monthlyLimit: Double
)