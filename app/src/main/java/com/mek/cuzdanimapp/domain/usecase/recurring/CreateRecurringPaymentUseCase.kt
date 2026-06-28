package com.mek.cuzdanimapp.domain.usecase.recurring

import com.mek.cuzdanimapp.domain.model.RecurringPayment
import com.mek.cuzdanimapp.domain.repository.RecurringPaymentRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class CreateRecurringPaymentUseCase @Inject constructor(
    private val repository: RecurringPaymentRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String?,
        amount: Double,
        category: String,
        type: String,
        frequency: String,
        startDate: String
    ): Resource<RecurringPayment> {
        return repository.createRecurringPayment(
            title, description, amount, category, type, frequency, startDate
        )
    }
}