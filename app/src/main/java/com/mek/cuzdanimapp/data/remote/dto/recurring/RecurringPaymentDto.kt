package com.mek.cuzdanimapp.data.remote.dto.recurring

data class RecurringPaymentDto(
    val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val frequency: String,
    val startDate: String,
    val lastGeneratedDate: String?,
    val active: Boolean
)
