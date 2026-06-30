package com.mek.cuzdanimapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF003527),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFF80BEA6),
    secondary = Color(0xFF4059AA),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF7F9FB),
    onBackground = Color(0xFF191C1E),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF191C1E),
    surfaceVariant = Color(0xFFE0E3E5),
    onSurfaceVariant = Color(0xFF404944),
    outline = Color(0xFF707974),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF95D3BA),
    onPrimary = Color(0xFF003527),
    primaryContainer = Color(0xFF0B513D),
    onPrimaryContainer = Color(0xFFB0F0D6),
    secondary = Color(0xFFB6C4FF),
    onSecondary = Color(0xFF264191),
    background = Color(0xFF191C1E),
    onBackground = Color(0xFFE2E2E5),
    surface = Color(0xFF1E2123),
    onSurface = Color(0xFFE2E2E5),
    surfaceVariant = Color(0xFF404944),
    onSurfaceVariant = Color(0xFFBFC9C3),
    outline = Color(0xFF8A938D),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun CuzdanimAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    androidx.compose.runtime.CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}