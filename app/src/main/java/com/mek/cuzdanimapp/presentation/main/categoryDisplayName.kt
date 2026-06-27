package com.mek.cuzdanimapp.presentation.main

val incomeCategories = listOf(
    "SALARY", "FREELANCE", "INVESTMENT", "SCHOLARSHIP", "BONUS", "OTHER"
)

val expenseCategories = listOf(
    "GROCERIES", "FOOD", "TRANSPORTATION", "FUEL", "HEALTH",
    "EDUCATION", "ENTERTAINMENT", "RENT", "BILLS", "SHOPPING", "OTHER"
)

fun categoryDisplayName(category: String): String {
    return when (category) {
        "SALARY" -> "Maaş"
        "FREELANCE" -> "Freelance"
        "INVESTMENT" -> "Yatırım"
        "SCHOLARSHIP" -> "Burs"
        "BONUS" -> "Prim"
        "GROCERIES" -> "Market"
        "FOOD" -> "Yemek"
        "TRANSPORTATION" -> "Ulaşım"
        "FUEL" -> "Yakıt"
        "HEALTH" -> "Sağlık"
        "EDUCATION" -> "Eğitim"
        "ENTERTAINMENT" -> "Eğlence"
        "RENT" -> "Kira"
        "BILLS" -> "Fatura"
        "SHOPPING" -> "Alışveriş"
        "OTHER" -> "Diğer"
        else -> category
    }
}