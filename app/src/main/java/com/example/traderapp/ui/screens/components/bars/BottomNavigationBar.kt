package com.example.traderapp.ui.screens.components.bars

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.graphics.Color
import com.example.traderapp.R
import java.util.Locale

@Composable
fun BottomNavigationBar(navController: NavController) {
    // get the route fromt the back stack
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = Color.Transparent
    ) {

        listOf(
            "home" to painterResource(id = R.drawable.home_icon),
            "trade" to painterResource(id = R.drawable.tr_icon),
            "market" to painterResource(id = R.drawable.market_icon),
            "favorites" to painterResource(id = R.drawable.favorites_icon),
            "wallet" to painterResource(id = R.drawable.wallet_icon),
        ).forEach { (screen, icon) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = icon,
                        contentDescription = screen,
                        modifier = Modifier.size(28.dp),
                        tint = if (currentRoute == screen) Color.Green else Color.Unspecified // Цвет иконки
                    )
                },
                label = { Text(screen.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }) },
                selected = false,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
