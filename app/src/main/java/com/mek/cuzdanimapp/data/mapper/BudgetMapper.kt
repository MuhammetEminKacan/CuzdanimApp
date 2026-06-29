package com.mek.cuzdanimapp.data.mapper

import com.mek.cuzdanimapp.data.remote.dto.budget.BudgetDto
import com.mek.cuzdanimapp.domain.model.Budget

fun BudgetDto.toDomain() = Budget(
    id = id,
    category = category,
    monthlyLimit = monthlyLimit,
    spentAmount = spentAmount,
    remainingAmount = remainingAmount,
    usagePercentage = usagePercentage
)