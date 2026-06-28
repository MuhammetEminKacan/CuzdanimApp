package com.mek.cuzdanimapp.presentation.recurring

sealed class RecurringPaymentEvent {
    data object LoadRecurringPayments : RecurringPaymentEvent()
    data object Refresh : RecurringPaymentEvent()
    data class ToggleActive(val id: Long) : RecurringPaymentEvent()
    data class Delete(val id: Long) : RecurringPaymentEvent()
}