// MainActivity.kt
package com.composeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.composeapp.data.local.ThemePreference
import com.composeapp.theme.ComposeAppTheme
import com.composeapp.ui.home.HomeScreen
import com.composeapp.ui.login.LoginScreen
import com.composeapp.ui.navigation.Screen
import com.composeapp.ui.notes.NotesScreen
import com.composeapp.ui.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreference: ThemePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by themePreference.isDarkMode

            ComposeAppTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()

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
                }
            }
        }
    }
}