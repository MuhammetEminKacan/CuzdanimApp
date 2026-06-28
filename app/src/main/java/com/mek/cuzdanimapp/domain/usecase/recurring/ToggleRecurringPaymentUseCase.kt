package com.mek.cuzdanimapp.domain.usecase.recurring

import com.mek.cuzdanimapp.domain.model.RecurringPayment
import com.mek.cuzdanimapp.domain.repository.RecurringPaymentRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class ToggleRecurringPaymentUseCase @Inject constructor(
    private val repository: RecurringPaymentRepository
) {
    suspend operator fun invoke(id: Long): Resource<RecurringPayment> {
        return repository.toggleRecurringPayment(id)
    }
}