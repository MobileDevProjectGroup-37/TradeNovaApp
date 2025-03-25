package com.example.traderapp.ui.screens.components



import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        listOf(
            "Home" to Icons.Default.Home,
            "Trade" to Icons.Default.ThumbUp,
            "Market" to Icons.Default.Lock,
            "Favorites" to Icons.Default.Star
        ).forEach { (screen, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = screen) },
                label = { Text(screen) },
                selected = false,
                onClick = {
                    navController.navigate(screen)
                }
            )
        }
    }
}
