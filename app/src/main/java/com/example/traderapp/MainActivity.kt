package com.example.traderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.traderapp.scaffold.AppNavigation
import com.example.traderapp.ui.theme.TraderAppTheme
import com.example.traderapp.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentTheme by themeViewModel.theme.collectAsState()

            TraderAppTheme(appTheme = currentTheme) {
                AppNavigation()
            }
        }
    }
}

