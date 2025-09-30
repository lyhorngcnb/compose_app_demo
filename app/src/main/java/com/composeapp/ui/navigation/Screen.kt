// ui/navigation/Screen.kt
//package com.composeapp.ui.navigation
//
//sealed class Screen(val route: String) {
//    object Splash : Screen("splash")
//    object Login : Screen("login")
//    object Home : Screen("home")
//    object Notes : Screen("notes")
//}
// ui/navigation/Screen.kt
package com.composeapp.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Notes : Screen("notes")
//    object Tasks : Screen("tasks")
//    object Calendar : Screen("calendar")
//    object Profile : Screen("profile")
//    object Settings : Screen("settings")
//    object Help : Screen("help")
    object Second : Screen("second")
}

