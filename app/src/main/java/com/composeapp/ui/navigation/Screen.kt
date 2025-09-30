// ui/navigation/Screen.kt
package com.composeapp.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Notes : Screen("notes")
}