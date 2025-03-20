package com.example.traderapp.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.ui.screens.LoginScreen
import com.example.traderapp.ui.screens.RegisterScreen
import com.example.traderapp.ui.screens.components.HomeScreen
import com.example.traderapp.ui.screens.components.OnBoardingScreen
import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    // val bottomBarViewModel: BottomBarViewModel = viewModel() // for future

    // val currentRoute = bottomBarViewModel.currentRoute
    // val isAuthenticated = authViewModel.isAuthenticated.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            if (isAuthenticated) {
//                TopAppBarComponent(
//                    navController = navController,
//                    authViewModel = authViewModel,
//                    bottomBarViewModel = bottomBarViewModel,
//                    currentRoute = currentRoute
//                )
//            }
//        },
        content = { paddingValues ->
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
                composable("login") { LoginScreen(navController, authViewModel) }
                composable("register") { RegisterScreen(navController, authViewModel) }

//                composable("forgot_password") { ForgotPasswordScreen(navController)

            }
        },
//        bottomBar = {
//            if (isAuthenticated) { BottomBar(navController) }
//        }
    )
}






