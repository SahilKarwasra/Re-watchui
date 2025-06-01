package com.example.re_watch.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkBluePrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkBluePrimaryVariant,
    onPrimaryContainer = DarkOnBackground,
    secondary = DarkBlueSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkBlueSecondaryVariant,
    onSecondaryContainer = DarkOnBackground,
    tertiary = DarkBlueAccent,
    onTertiary = DarkOnSecondary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkOnSurface,
    error = DarkError,
    onError = DarkOnPrimary,
    outline = DividerColor,
    outlineVariant = ShimmerColor
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = BluePrimaryVariant,
    onPrimaryContainer = LightOnPrimary,
    secondary = BlueSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = BlueSecondaryVariant,
    onSecondaryContainer = LightOnSecondary,
    tertiary = BlueAccent,
    onTertiary = LightOnSecondary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurface,
    onSurfaceVariant = LightOnSurface,
    error = LightError,
    onError = LightOnPrimary,
    outline = DividerColor,
    outlineVariant = ShimmerColor
)

@Composable
fun RewatchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use our custom blue theme
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
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color to transparent for edge-to-edge
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            
            // Configure system bars appearance
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}