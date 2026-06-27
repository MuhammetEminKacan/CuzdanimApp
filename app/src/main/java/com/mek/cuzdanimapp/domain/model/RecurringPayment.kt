package com.mek.cuzdanimapp.domain.model

data class RecurringPayment(
    val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val frequency: String,
    val startDate: String,
    val lastGeneratedDate: String?,
    val active: Boolean
)
