package com.example.traderapp.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.ui.screens.HomeScreen
import com.example.traderapp.ui.screens.MarketScreen
import com.example.traderapp.ui.screens.OnBoardingScreen
import com.example.traderapp.ui.screens.SettingsScreen
import com.example.traderapp.ui.screens.authentication.*
import com.example.traderapp.ui.screens.authentication.RegisterScreen
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.utils.Constants
import com.example.traderapp.viewmodel.AuthViewModel
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.OnBoardingViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    //Create one hiltViewModel for the whole app
    val authViewModel: AuthViewModel = hiltViewModel()


    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    Scaffold(
        topBar = {
            val currentRoute = navController.currentDestination?.route
            when (currentRoute) {
                Constants.LOGIN_SCREEN_ROUTE -> AppTopBar(title = "Login", showBackButton = false)
                "register" -> AppTopBar(
                    title = "Register",
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() }
                )
                Constants.HOME_SCREEN_ROUTE -> AppTopBar(title = "Home", showBackButton = false)
                else -> {}
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) {
                Constants.HOME_SCREEN_ROUTE
            } else {
                Constants.ONBOARDING_SCREEN_ROUTE
            },
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Constants.HOME_SCREEN_ROUTE) {
                HomeScreen(navController)
            }
            composable("market") {
                val cryptoViewModel: CryptoViewModel = hiltViewModel()
                MarketScreen(navController, cryptoViewModel)
            }

            composable(Constants.ONBOARDING_SCREEN_ROUTE) {
                val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(navController, onBoardingViewModel)
            }
            composable("welcome") {
                WelcomeScreen(navController)
            }
            composable(Constants.LOGIN_SCREEN_ROUTE) {
                // ✅ Передаём уже созданный authViewModel
                LoginScreen(navController, authViewModel)
            }
            composable("account_creation") {
                AccountCreationScreen(navController)
            }

            // ✅ Передаём наш общий authViewModel
            composable("add_mail") {
                AddMail(navController, authViewModel)
            }
            composable("confirm_mail") {
                ConfirmMail(navController, authViewModel)
            }

            composable("enter_code") {
                EnterCode(navController, authViewModel)
            }
            composable("create_password") {
                CreatePassword(navController, authViewModel)
            }
            composable("successful_registration") {
                SuccessfulRegistration(navController)
            }
            composable("register") {
                RegisterScreen(navController, authViewModel)
            }
            composable(Constants.SETTINGS_SCREEN_ROUTE) {
                SettingsScreen(navController, authViewModel)
            }
        }
    }
}
