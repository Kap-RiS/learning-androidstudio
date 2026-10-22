package com.example.learn_androidstudio.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RuStoreBlue,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DarkBackground,
)

private val LightColorScheme = lightColorScheme(
    primary = RuStoreBlue,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = LightBackground, // Белый фон
    surface = Color(0xFFF5F6F8),   // Чистый светло-серый фон для карточек
    surfaceTint = Color.Transparent // Отключаем розовый/фиолетовый оттенок от elevation в Material 3
)

@Composable
fun Learn_androidstudioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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