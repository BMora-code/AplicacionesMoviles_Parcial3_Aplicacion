package com.example.aplicacion.navigation

/**
 * Define las rutas de navegación de la aplicación.
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Location : Screen("location")
}