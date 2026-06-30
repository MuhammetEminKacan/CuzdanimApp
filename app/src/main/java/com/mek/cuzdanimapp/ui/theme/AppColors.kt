package com.mek.cuzdanimapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val income: Color,
    val expense: Color,
    val warning: Color
)

val LightAppColors = AppColors(
    income = Color(0xFF2E7D32),
    expense = Color(0xFFD32F2F),
    warning = Color(0xFFE65100)
)

val DarkAppColors = AppColors(
    income = Color(0xFF81C784),
    expense = Color(0xFFEF9A9A),
    warning = Color(0xFFFFB74D)
)

val LocalAppColors = compositionLocalOf { LightAppColors }

val MaterialTheme.appColors: AppColors
    @Composable
    get() = LocalAppColors.current