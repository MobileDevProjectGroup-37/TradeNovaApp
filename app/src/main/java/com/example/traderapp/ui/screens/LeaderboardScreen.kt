package com.example.traderapp.ui.screens


// --- Jetpack Compose
import LeaderboardItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*

// --- For alignment, arrangement, spacing
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// --- Hilt ViewModel
import androidx.hilt.navigation.compose.hiltViewModel

// --- Logging
import android.util.Log

// --- Colors (you can place them in a separate Theme file if you prefer)
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.traderapp.R

// --- Our custom ViewModel & Data
import com.example.traderapp.data.model.UserData
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.viewmodel.LeaderboardViewModel
val UpholdGreen = Color(0xFF0EAD69)
val UpholdLightGreen = Color(0xFF7CDB8A)
val UpholdGrayBackground = Color(0xFFF5F5F5)
val UpholdWhite = Color(0xFFFFFFFF)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel
) {
    // Collect state from our ViewModel
    val leaderboard by viewModel.leaderboard.collectAsState()

    // Scaffold with a top bar
    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.BACK,
                rightIconType = RightIconType.UNION,
                onBackClick = { navController.popBackStack() },
                onRightClick = { /* action */ },
                title = "Market"
            )
        },

    ) { paddingValues ->
        // Main content area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // If empty, show a loading or empty view
            if (leaderboard.isEmpty()) {
                // Simple "empty" or "loading" placeholder
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = UpholdGreen
                )
            } else {
                // Otherwise, show a list of users
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    itemsIndexed(leaderboard) { index, user ->
                        LeaderboardItem(
                            rank = index + 1,
                            userData = user
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
