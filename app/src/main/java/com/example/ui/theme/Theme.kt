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

private val LightColorScheme = lightColorScheme(
    primary = VibrantPurple,
    onPrimary = Color.White,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = OnPurpleContainer,
    secondary = PurpleDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDDD6FE),
    onSecondaryContainer = Color(0xFF3B0764),
    tertiary = AccentCoral,
    onTertiary = Color.White,
    tertiaryContainer = CoralContainer,
    onTertiaryContainer = OnCoralContainer,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    outlineVariant = Color(0xFFE2E8F0)
)

private val DarkColorScheme = darkColorScheme(
    primary = PurpleLight,
    onPrimary = Color(0xFF2E1065),
    primaryContainer = Color(0xFF5B21B6),
    onPrimaryContainer = PurpleContainer,
    secondary = VibrantPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4C1D95),
    onSecondaryContainer = Color(0xFFDDD6FE),
    tertiary = AccentCoral,
    onTertiary = Color(0xFF4C0519),
    tertiaryContainer = Color(0xFF881337),
    onTertiaryContainer = CoralContainer,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF475569)
)

@Composable
fun EventHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep signature vibrant purple & coral palette
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
