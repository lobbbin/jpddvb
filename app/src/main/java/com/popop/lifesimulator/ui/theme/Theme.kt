package com.popop.lifesimulator.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = RoyalPurpleLight,
    secondary = PoliticalBlueLight,
    tertiary = BusinessGreenLight,
    background = PrimaryBackground,
    surface = SecondaryBackground,
    onPrimary = PrimaryText,
    onSecondary = PrimaryText,
    onBackground = PrimaryText,
    onSurface = PrimaryText,
    error = ErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalPurple,
    secondary = PoliticalBlue,
    tertiary = BusinessGreen,
    background = Color(0xFFF7FAFC),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A202C),
    onSurface = Color(0xFF1A202C),
    error = ErrorRed
)

@Composable
fun LifeSimulatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
