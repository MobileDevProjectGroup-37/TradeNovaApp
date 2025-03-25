package com.example.traderapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.ui.screens.components.CryptoItem
import com.example.traderapp.ui.screens.components.BottomNavigationBar

import com.example.traderapp.viewmodel.CryptoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(navController: NavController, viewModel: CryptoViewModel) {
    val cryptoList by viewModel.cryptoList.collectAsState()
    val priceUpdates by viewModel.priceUpdates.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Market") }
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
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(cryptoList) { crypto ->
                    CryptoItem(
                        crypto = crypto,
                        currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDouble() //
                    )
                }
            }
        }
    }
}
