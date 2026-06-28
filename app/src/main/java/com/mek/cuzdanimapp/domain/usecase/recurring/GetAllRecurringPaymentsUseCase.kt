package com.mek.cuzdanimapp.domain.usecase.recurring

import com.mek.cuzdanimapp.domain.model.RecurringPayment
import com.mek.cuzdanimapp.domain.repository.RecurringPaymentRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class GetAllRecurringPaymentsUseCase @Inject constructor(
    private val repository: RecurringPaymentRepository
) {
    suspend operator fun invoke(): Resource<List<RecurringPayment>> {
        return repository.getAllRecurringPayments()
    }
}