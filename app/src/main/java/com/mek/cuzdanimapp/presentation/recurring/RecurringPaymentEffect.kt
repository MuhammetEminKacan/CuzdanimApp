package com.mek.cuzdanimapp.presentation.recurring

sealed class RecurringPaymentEffect {
    data class ShowError(val message: String) : RecurringPaymentEffect()
    data object Deleted : RecurringPaymentEffect()
}