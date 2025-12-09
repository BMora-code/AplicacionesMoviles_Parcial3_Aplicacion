package com.example.aplicacion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.ui.screens.*
import com.example.aplicacion.viewmodel.AppViewModel

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // ViewModel global de la app
    val appViewModel: AppViewModel = viewModel(factory = AppViewModel.factory(context))

    // Leemos si el usuario está logeado o no
    val session by appViewModel.sessionState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (session.first) Screen.Home.route else Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController, appViewModel)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController, appViewModel)
        }

        composable(Screen.Location.route) {
            LocationScreen(navController)
        }
    }
}