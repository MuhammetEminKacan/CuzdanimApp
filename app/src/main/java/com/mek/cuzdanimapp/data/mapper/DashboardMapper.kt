package com.mek.cuzdanimapp.data.mapper

import com.mek.cuzdanimapp.data.remote.dto.dashboard.DashboardResponseDto
import com.mek.cuzdanimapp.data.remote.dto.recurring.RecurringPaymentDto
import com.mek.cuzdanimapp.data.remote.dto.transaction.TransactionDto
import com.mek.cuzdanimapp.domain.model.DashboardData
import com.mek.cuzdanimapp.domain.model.RecurringPayment
import com.mek.cuzdanimapp.domain.model.Transaction


fun TransactionDto.toDomain() = Transaction(
    id = id,
    type = type,
    category = category,
    amount = amount,
    description = description,
    transactionDate = transactionDate
)

fun RecurringPaymentDto.toDomain() = RecurringPayment(
    id = id,
    title = title,
    amount = amount,
    category = category,
    frequency = frequency,
    startDate = startDate,
    lastGeneratedDate = lastGeneratedDate,
    active = active
)

fun DashboardResponseDto.toDomain() = DashboardData(
    totalBalance = totalBalance,
    monthlyIncome = monthlyIncome,
    monthlyExpense = monthlyExpense,
    monthlySavings = monthlySavings,
    recentTransactions = recentTransactions.map { it.toDomain() },
    upcomingPayments = upcomingPayments.map { it.toDomain() }
)