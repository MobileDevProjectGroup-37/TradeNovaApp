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
import com.example.traderapp.data.network.UserSessionViewModel
import com.example.traderapp.ui.screens.HomeScreen
import com.example.traderapp.ui.screens.LeaderboardScreen
import com.example.traderapp.ui.screens.MarketScreen
import com.example.traderapp.ui.screens.OnBoardingScreen
import com.example.traderapp.ui.screens.SettingsScreen

import com.example.traderapp.ui.screens.authentication.*
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.trade.TradeScreen

import com.example.traderapp.utils.Constants
import com.example.traderapp.viewmodel.AuthViewModel
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.LeaderboardViewModel
import com.example.traderapp.viewmodel.OnBoardingViewModel
import com.example.traderapp.viewmodel.TradeViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    //Create one hiltViewModel for the whole app
    val authViewModel: AuthViewModel = hiltViewModel()
    val cryptoViewModel: CryptoViewModel = hiltViewModel()
    val tardeViewModel: TradeViewModel = hiltViewModel()
    val userSessionViewModel: UserSessionViewModel = hiltViewModel()
    val userSession = userSessionViewModel.userSession
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val leaderboardViewModel: LeaderboardViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            val currentRoute = navController.currentDestination?.route
            when (currentRoute) {
                Constants.LOGIN_SCREEN_ROUTE -> AppTopBar(title = "Login", showBackButton = false)
                Constants.REGISTER_SCREEN_ROUTE -> AppTopBar(
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
                HomeScreen(navController, cryptoViewModel,tardeViewModel,userSession)
            }
            composable(Constants.MARKET_SCREEN_ROUTE)  {
                MarketScreen(navController, cryptoViewModel)
            }

            composable(Constants.TRADE_SCREEN_ROUTE) {
                TradeScreen(navController, cryptoViewModel, tardeViewModel, userSession)
            }

            composable(Constants.ONBOARDING_SCREEN_ROUTE) {
                val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(navController, onBoardingViewModel)
            }

            composable(Constants.WELCOME_SCREEN_ROUTE) {
                WelcomeScreen(navController)
            }
            composable(Constants.LOGIN_SCREEN_ROUTE) {
                LoginScreen(navController, authViewModel)
            }
            composable(Constants.ACCOUNT_CREATION_SCREEN_ROUTE) {
                AccountCreationScreen(navController)
            }
            composable(Constants.ADD_MAIL_SCREEN_ROUTE) {
                AddMail(navController, authViewModel)
            }
            composable(Constants.CONFIRM_MAIL_SCREEN_ROUTE) {
                ConfirmMail(navController, authViewModel)
            }
            composable(Constants.ENTER_CODE_SCREEN_ROUTE) {
                EnterCode(navController, authViewModel)
            }
            composable(Constants.CREATE_PASSWORD_SCREEN_ROUTE) {
                CreatePassword(navController, authViewModel)
            }
            composable(Constants.SUCCESSFUL_REGISTRATION_SCREEN_ROUTE) {
                SuccessfulRegistration(navController)
            }
            composable(Constants.REGISTER_SCREEN_ROUTE) {
                RegisterScreen(navController, authViewModel)
            }
            composable(Constants.SETTINGS_SCREEN_ROUTE) {
                SettingsScreen(navController, authViewModel)
            }
            composable("reset_password") {
              ResetPasswordScreen(navController, authViewModel)
            }


            composable(Constants.LEADERBOARD_SCREEN_ROUTE) {
                LeaderboardScreen(navController, leaderboardViewModel)
            }
        }
    }
}
