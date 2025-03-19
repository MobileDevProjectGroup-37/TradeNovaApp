package com.example.traderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.scaffold.AppNavigation

import com.example.traderapp.ui.theme.TraderAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraderAppTheme {
                // Создаём NavController
                val navController = rememberNavController()

                // Запускаем навигацию
                AppNavigation(navController)
            }
        }
    }
}
