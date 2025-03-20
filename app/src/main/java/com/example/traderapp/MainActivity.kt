package com.example.traderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.traderapp.scaffold.AppNavigation

import com.example.traderapp.ui.theme.TraderAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraderAppTheme {
                AppNavigation()
            }
        }
    }
}

