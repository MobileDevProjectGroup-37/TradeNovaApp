package com.example.traderapp.scaffold


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.ui.screens.authentication.AccountCreationScreen
import com.example.traderapp.ui.screens.HomeScreen
import com.example.traderapp.ui.screens.authentication.LoginScreen
import com.example.traderapp.ui.screens.MarketScreen
import com.example.traderapp.ui.screens.OnBoardingScreen
import com.example.traderapp.ui.screens.authentication.RegisterScreen
import com.example.traderapp.ui.screens.authentication.WelcomeScreen
import com.example.traderapp.ui.screens.components.bars.AppTopBar

import com.example.traderapp.ui.screens.SettingsScreen
import com.example.traderapp.ui.screens.authentication.AddMail
import com.example.traderapp.ui.screens.authentication.ConfirmMail
import com.example.traderapp.ui.screens.authentication.CreatePassword
import com.example.traderapp.ui.screens.authentication.ResetPasswordScreen
import com.example.traderapp.ui.screens.authentication.EnterCode
import com.example.traderapp.ui.screens.authentication.SuccessfulRegistration
import com.example.traderapp.utils.Constants
import com.example.traderapp.viewmodel.AuthViewModel
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.OnBoardingViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    // val bottomBarViewModel: BottomBarViewModel = viewModel() // for future

    // val currentRoute = bottomBarViewModel.currentRoute
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState() // Используем isLoggedIn

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
    ){ paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Constants.HOME_SCREEN_ROUTE else Constants.ONBOARDING_SCREEN_ROUTE, // Используем isLoggedIn
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Constants.HOME_SCREEN_ROUTE)  { HomeScreen( navController) }
            composable(route = "market") {
                val cryptoViewModel: CryptoViewModel = viewModel()
                MarketScreen(navController, cryptoViewModel)
            }
//
//                composable("favorite") { OrdersScreen(paddingValues) }
//                composable("profile") { ProfileScreen(navController) }
//                composable(route = "trade")  { TradeScreen(navController) }
//                composable(route = "settings")  { SettingsScreen(navController) }
//

            composable(Constants.ONBOARDING_SCREEN_ROUTE) {
                val onBoardingViewModel: OnBoardingViewModel = viewModel()
                OnBoardingScreen(navController, onBoardingViewModel)
            }
            composable("welcome") { WelcomeScreen(navController) }
            composable(Constants.LOGIN_SCREEN_ROUTE) { LoginScreen(navController, authViewModel) }
            composable("account_creation") { AccountCreationScreen(navController) }
            composable("add_mail") { AddMail(navController) }
            composable("confirm_mail") { ConfirmMail(navController) }
            composable("enter_code") { EnterCode(navController) }
            composable("create_password") { CreatePassword(navController) }
            composable("successful_registration") { SuccessfulRegistration(navController) }
            composable("register") { RegisterScreen(navController, authViewModel) }
            composable(Constants.SETTINGS_SCREEN_ROUTE) { SettingsScreen(navController, authViewModel) }
            composable("reset_password") { ResetPasswordScreen(navController, authViewModel) }

//                composable("forgot_password") { ForgotPasswordScreen(navController)

        }
    }
//        bottomBar = {
//            if (isLoggedIn) { BottomBar(navController) }
//        }

}