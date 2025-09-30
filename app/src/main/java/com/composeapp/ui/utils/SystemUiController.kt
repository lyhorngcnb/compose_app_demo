// ui/utils/SystemUiController.kt
package com.composeapp.ui.utils

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun setStatusBarColor(
    color: Color = Color.Transparent,
    darkIcons: Boolean = true
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as androidx.activity.ComponentActivity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = color.copy(alpha = 1.0f).value.toInt()

            // Set status bar icons color (light/dark)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkIcons
        }
    }
}

@Composable
fun setNavigationBarColor(
    color: Color = Color.Transparent,
    darkIcons: Boolean = true
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as androidx.activity.ComponentActivity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.navigationBarColor = color.copy(alpha = 1.0f).value.toInt()

            // Set navigation bar icons color (light/dark)
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = darkIcons
        }
    }
}