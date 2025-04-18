package com.example.traderapp.ui.screens.market

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.ui.screens.components.cards.CryptoItem
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.screens.components.bars.SearchBar
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.viewmodel.CryptoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(navController: NavController, viewModel: CryptoViewModel) {
    val cryptoList by viewModel.cryptoList.collectAsState()
    val priceUpdates by viewModel.priceUpdates.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    TransparentStatusBar()

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
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {

            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = { query -> searchQuery = query }
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    MarketHeader()
                }

                items(cryptoList.filter { crypto ->
                    crypto.name.contains(searchQuery, ignoreCase = true)
                }) { crypto ->
                    CryptoItem(
                        crypto = crypto,
                        currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDouble()
                    )
                }
            }
        }
    }
}
