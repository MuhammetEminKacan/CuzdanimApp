package com.mek.cuzdanimapp.domain.repository

import com.mek.cuzdanimapp.domain.model.RecurringPayment
import com.mek.cuzdanimapp.util.Resource

interface RecurringPaymentRepository {
    suspend fun getAllRecurringPayments(): Resource<List<RecurringPayment>>
    suspend fun createRecurringPayment(
        title: String,
        description: String?,
        amount: Double,
        category: String,
        type: String,
        frequency: String,
        startDate: String
    ): Resource<RecurringPayment>
    suspend fun toggleRecurringPayment(id: Long): Resource<RecurringPayment>
    suspend fun deleteRecurringPayment(id: Long): Resource<Unit>
}