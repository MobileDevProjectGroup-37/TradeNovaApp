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
        //login
        composable(route = "login") {
            LoginScreen(
                onBackClick = { navController.navigateUp() },
                onSignInClick = { email, password ->
                    // TODO logic with firebase (Firebase/AuthViewModel)
                },
                onForgotPasswordClick = {
                    // if  "forgot" needed, do navController.navigate("forgot")
                },
                onGoogleClick = {
                    // Logic Google Sign-In
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        // register
        composable(route = "register") {
            RegisterScreen(
                onBackClick = { navController.navigateUp() },
                onSignUpWithEmail = { email, password ->
                    // TODO register via firebase
                },
                onGoogleClick = {
                    // logic google sign in
                },
                onSignInClick = {
                    // back to login
                    navController.navigate("login")
                }
            )
        }
    }
}
