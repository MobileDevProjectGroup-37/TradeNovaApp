package com.example.traderapp.ui.screens.portfolio

import SetStatusBarColor
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.data.model.UserProfile
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.screens.components.cards.UserProfileCard

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    navController: NavController
) {
    SetStatusBarColor()

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.BACK,
                rightIconType = RightIconType.SETTINGS,
                onBackClick = { navController.popBackStack() },
                onRightClick = { navController.navigate("settings") },
                title = "Profile",

            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            UserProfileCard(userProfile)
        }
    }
}
