package com.mek.cuzdanimapp.presentation.recurring

import com.mek.cuzdanimapp.domain.model.RecurringPayment

data class RecurringPaymentState(
    val isLoading: Boolean = false,
    val recurringPayments: List<RecurringPayment> = emptyList(),
    val errorMessage: String? = null
)