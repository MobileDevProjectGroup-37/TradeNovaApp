package com.example.traderapp.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.ui.screens.LoginScreen
import com.example.traderapp.ui.screens.RegisterScreen
import com.example.traderapp.ui.screens.WelcomeScreen
import com.example.traderapp.ui.screens.components.AppTopBar
import com.example.traderapp.ui.screens.components.HomeScreen
import com.example.traderapp.ui.screens.components.OnBoardingScreen
import com.example.traderapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    // val bottomBarViewModel: BottomBarViewModel = viewModel() // for future

    // val currentRoute = bottomBarViewModel.currentRoute
    // val isAuthenticated = authViewModel.isAuthenticated.value

    Scaffold(
        topBar = {
            val currentRoute = navController.currentDestination?.route
            when (currentRoute) {
                "login" -> AppTopBar(title = "Login", showBackButton = false)
                "register" -> AppTopBar(
                    title = "Register",
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() }
                )
                "home" -> AppTopBar(title = "Home", showBackButton = false)
                else -> {}
            }
        }
    ){ paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "onboarding",
                //startDestination = if (isAuthenticated) "home" else "onBoarding",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = "home")  { HomeScreen( paddingValues) }
//                composable(route = "market")  { MarketScreen(navController, ) }
//                composable("favorite") { OrdersScreen(paddingValues) }
//                composable("profile") { ProfileScreen(navController) }
//                composable(route = "trade")  { TradeScreen(navController) }
//                composable(route = "settings")  { SettingsScreen(navController) }
//

                composable("onboarding") {
                    OnBoardingScreen(navController = navController)
                }
                composable("welcome") { WelcomeScreen(navController) }
                composable("login") { LoginScreen(navController, authViewModel) }
                composable("register") { RegisterScreen(navController, authViewModel) }

//                composable("forgot_password") { ForgotPasswordScreen(navController)

            }
        }
//        bottomBar = {
//            if (isAuthenticated) { BottomBar(navController) }
//        }

}






