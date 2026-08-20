package com.example.ui.theme

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
    primary = YatraaSaffron,
    onPrimary = Color.Black,
    primaryContainer = YatraaSaffronDark,
    onPrimaryContainer = Color.White,
    secondary = YatraaEmerald,
    onSecondary = Color.Black,
    secondaryContainer = YatraaNavyCard,
    onSecondaryContainer = Color.White,
    tertiary = YatraaGold,
    background = YatraaNavy,
    onBackground = Color.White,
    surface = YatraaNavySurface,
    onSurface = Color.White,
    surfaceVariant = YatraaNavyCard,
    onSurfaceVariant = Slate400,
    error = YatraaCoral,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = YatraaSaffronDark,
    onPrimary = Color.White,
    primaryContainer = YatraaSaffronLight,
    onPrimaryContainer = Color(0xFF78350F),
    secondary = YatraaEmerald,
    onSecondary = Color.White,
    secondaryContainer = YatraaEmeraldLight,
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = YatraaNavy,
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate600,
    error = YatraaCoral,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted mobility identity colors
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
