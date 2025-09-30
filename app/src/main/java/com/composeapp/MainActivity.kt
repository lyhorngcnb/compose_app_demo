// MainActivity.kt
package com.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.composeapp.data.local.ThemePreference
import com.composeapp.theme.ComposeAppTheme
import com.composeapp.ui.home.HomeScreen
import com.composeapp.ui.login.LoginScreen
import com.composeapp.ui.navigation.Screen
import com.composeapp.ui.notes.NotesScreen
import com.composeapp.ui.screens.SecondScreen
import com.composeapp.ui.splash.SplashScreen
import com.composeapp.ui.utils.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreference: ThemePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Make status bar transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val isDarkMode by themePreference.isDarkMode

            ComposeAppTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()

                // Set transparent status bar with appropriate icon colors
                setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkMode // Light icons in dark mode, dark icons in light mode
                )

                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route
                ) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navController)
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(navController)
                    }
                    composable(Screen.Home.route) {
                        HomeScreen(navController, themePreference)
                    }
                    composable(Screen.Notes.route) {
                        NotesScreen(navController)
                    }
                    composable(Screen.Second.route) {
                        SecondScreen(navController)
                    }
                }
            }
        }
    }
}