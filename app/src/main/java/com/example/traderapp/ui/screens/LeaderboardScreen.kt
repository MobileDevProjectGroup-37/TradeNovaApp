package com.example.traderapp.ui.screens

import LeaderboardItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.viewmodel.LeaderboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel
) {
    val leaderboard by viewModel.leaderboard.collectAsState()

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.BACK,
                rightIconType = RightIconType.UNION,
                onBackClick = { navController.popBackStack() },
                onRightClick = { /* TODO */ },
                title = "Market"
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (leaderboard.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
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
