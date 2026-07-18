package com.example.test_app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

/*
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7BBF9A),
    secondary = Color(0xFF5BA07A),
    tertiary = Color(0xFF98f469),
    background = Color(0xFF1A3A28),
    surface = Color(0xFF1A3A28),
    onSurface = Color(0xFFF7FAF8),
    primaryContainer = Color(0xFF5BA07A),
    onPrimaryContainer = Color.White,
    secondaryContainer = Color(0xFF2D501B),
    onSecondaryContainer = Color(0xFFB4F192),
    surfaceVariant = Color(0xFF234431),
    onSurfaceVariant = Color(0xFFC4C8BB)
)
*/

private val MintColorScheme = lightColorScheme(
    primary = Mint20,
    onPrimary = White100,
    secondary = Mint40,
    tertiary = Green70,
    background = White40,
    surface = White40,
    surfaceContainer = Mint20,
    onSurface = Green90, 
    primaryContainer = Mint40,
    onPrimaryContainer = White100,
    secondaryContainer = White20,
    onSecondaryContainer = Green90,
    surfaceVariant = White100,
    onSurfaceVariant = Grey90,
    outlineVariant = White10
)

private val SlateAmberColorScheme = lightColorScheme(
    primary = Grey50,
    onPrimary = White100,
    secondary = Mint40,
    tertiary = Green70,
    background = White50,
    surface = Grey50, 
    surfaceContainer = Grey50,
    onSurface = White100,
    primaryContainer = Orange60,
    onPrimaryContainer = White100,
    secondaryContainer = White50,
    onSecondaryContainer = Green90,
    surfaceVariant = White100,
    onSurfaceVariant = Grey90,
    outline = White100,
    outlineVariant = Grey90
)

enum class AppTheme(val displayName: String) {
    SageMint("Sage & Mint"),
    SlateAmber("Slate & Amber")
}

@Composable
fun Test_appTheme(
    selectedTheme: AppTheme = AppTheme.SlateAmber,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        //darkTheme -> LightColorScheme
        else -> when (selectedTheme) {
            AppTheme.SageMint -> MintColorScheme
            AppTheme.SlateAmber -> SlateAmberColorScheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
