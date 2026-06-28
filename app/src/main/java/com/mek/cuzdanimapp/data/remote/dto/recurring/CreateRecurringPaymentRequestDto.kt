package com.mek.cuzdanimapp.data.remote.dto.recurring

data class CreateRecurringPaymentRequestDto(
    val title: String,
    val description: String?,
    val amount: Double,
    val category: String,
    val type: String,
    val frequency: String,
    val startDate: String
)