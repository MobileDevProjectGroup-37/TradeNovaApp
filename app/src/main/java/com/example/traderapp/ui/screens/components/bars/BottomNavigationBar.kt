package com.example.traderapp.ui.screens.components.bars

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.traderapp.R
import com.example.traderapp.utils.Constants

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        Triple(Constants.HOME_SCREEN_ROUTE, R.drawable.home_icon, "Home"),
        Triple(Constants.TRADE_SCREEN_ROUTE, R.drawable.tr_icon, "Trade"),
        Triple(Constants.MARKET_SCREEN_ROUTE, R.drawable.market_icon, "Market"),
        Triple(Constants.LEADERBOARD_SCREEN_ROUTE, R.drawable.favorites_icon, "Rating")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEach { (route, iconId, labelText) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = labelText,
                        modifier = Modifier.size(28.dp),
                        tint = if (currentRoute == route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                },
                label = { Text(labelText) },
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
