package com.mek.cuzdanimapp.domain.model


data class DashboardData(
    val totalBalance: Double,
    val monthlyIncome: Double,
    val monthlyExpense: Double,
    val monthlySavings: Double,
    val recentTransactions: List<Transaction>,
    val upcomingPayments: List<RecurringPayment>
)
