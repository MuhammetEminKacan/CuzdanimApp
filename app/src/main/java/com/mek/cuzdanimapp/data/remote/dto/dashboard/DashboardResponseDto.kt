package com.mek.cuzdanimapp.data.remote.dto.dashboard

import com.mek.cuzdanimapp.data.remote.dto.recurring.RecurringPaymentDto
import com.mek.cuzdanimapp.data.remote.dto.transaction.TransactionDto

data class DashboardResponseDto(
    val totalBalance: Double,
    val monthlyIncome: Double,
    val monthlyExpense: Double,
    val monthlySavings: Double,
    val recentTransactions: List<TransactionDto>,
    val upcomingPayments: List<RecurringPaymentDto>
)
