package com.example.traderapp.scaffold

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.traderapp.ui.screens.LoginScreen
import com.example.traderapp.ui.screens.RegisterScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 1) Стартовый экран — логин
        composable(route = "login") {
            LoginScreen(
                onBackClick = { navController.navigateUp() },
                onSignInClick = { email, password ->
                    // При необходимости: логика входа (Firebase/AuthViewModel)
                },
                onForgotPasswordClick = {
                    // Если нужен экран "forgot", делаете navController.navigate("forgot")
                },
                onGoogleClick = {
                    // Логика Google Sign-In
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        // 2) Экран регистрации
        composable(route = "register") {
            RegisterScreen(
                onBackClick = { navController.navigateUp() },
                onSignUpWithEmail = { email, password ->
                    // При необходимости: логика регистрации
                },
                onGoogleClick = {
                    // Логика Google Sign-Up
                },
                onSignInClick = {
                    // Переходим обратно на логин
                    navController.navigate("login")
                }
            )
        }
    }
}
