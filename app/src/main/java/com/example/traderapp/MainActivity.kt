package com.example.traderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.traderapp.scaffold.AppNavigation

import com.example.traderapp.ui.theme.TraderAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

