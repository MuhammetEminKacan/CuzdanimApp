package com.mek.cuzdanimapp.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mek.cuzdanimapp.R

val incomeCategories = listOf(
    "SALARY", "FREELANCE", "INVESTMENT", "SCHOLARSHIP", "BONUS", "OTHER"
)

val expenseCategories = listOf(
    "GROCERIES", "FOOD", "TRANSPORTATION", "FUEL", "HEALTH",
    "EDUCATION", "ENTERTAINMENT", "RENT", "BILLS", "SHOPPING", "OTHER"
)

@Composable
fun categoryDisplayName(category: String): String {
    return when (category) {
        "SALARY" -> stringResource(R.string.category_salary)
        "FREELANCE" -> stringResource(R.string.category_freelance)
        "INVESTMENT" -> stringResource(R.string.category_investment)
        "SCHOLARSHIP" -> stringResource(R.string.category_scholarship)
        "BONUS" -> stringResource(R.string.category_bonus)
        "GROCERIES" -> stringResource(R.string.category_groceries)
        "FOOD" -> stringResource(R.string.category_food)
        "TRANSPORTATION" -> stringResource(R.string.category_transportation)
        "FUEL" -> stringResource(R.string.category_fuel)
        "HEALTH" -> stringResource(R.string.category_health)
        "EDUCATION" -> stringResource(R.string.category_education)
        "ENTERTAINMENT" -> stringResource(R.string.category_entertainment)
        "RENT" -> stringResource(R.string.category_rent)
        "BILLS" -> stringResource(R.string.category_bills)
        "SHOPPING" -> stringResource(R.string.category_shopping)
        "OTHER" -> stringResource(R.string.category_other)
        else -> category
    }
}