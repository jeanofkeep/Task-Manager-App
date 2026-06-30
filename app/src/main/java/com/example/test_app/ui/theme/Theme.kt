package com.example.test_app.ui.theme

import android.app.Activity
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
private val LightColorScheme = lightColorScheme(
    primary = Mint20,
    onPrimary = White100,
    secondary = Mint40,
    tertiary = Green70,
    background = White40,
    surface = Mint20, // Цвет для TopAppBar (шапка) //mint-green
    surfaceContainer = Mint20, //mint-green
    onSurface = Green90, // Основной текст (темно-зеленый)
    primaryContainer = Mint40, // Цвет для FAB (кнопка +)
    onPrimaryContainer = White100,
    secondaryContainer = White20, // Цвет индикатора в BottomBar
    onSecondaryContainer = Green90,
    surfaceVariant = White100, // Цвет для карточек задач (белый)
    onSurfaceVariant = Grey40,
    outlineVariant = White10 // Цвет обводки (светло-серый с оттенком мяты)
)
@Composable
fun Test_appTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}