package com.example.traderapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.ui.screens.components.MarketMoversSection
import com.example.traderapp.ui.screens.components.PortfolioBalanceSection
import com.example.traderapp.ui.screens.components.PortfolioSection
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.TradeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    cryptoViewModel: CryptoViewModel,
    tradeViewModel: TradeViewModel,
    userSession: UserSession
) {
    //region === Observing state from TradeViewModel ===
    val userBalance by tradeViewModel.userBalance.collectAsState()
    val portfolioValue by tradeViewModel.portfolioValue.collectAsState()
    val totalValue by tradeViewModel.totalValue.collectAsState()
    val percentChange by tradeViewModel.percentageChange.collectAsState()
    val isLoading by tradeViewModel.isLoading.collectAsState()
    //endregion

    //region === Observing state from CryptoViewModel ===
    val priceUpdates by cryptoViewModel.priceUpdates.collectAsState()
    val marketMovers by cryptoViewModel.marketMovers.collectAsState()
    //endregion

    //region === One-time effect to load data ===
    LaunchedEffect(Unit) {
        tradeViewModel.loadInitialData(cryptoViewModel)
    }
    //endregion

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.PROFILE,
                rightIconType = RightIconType.SETTINGS,
                onBackClick = { /* ... */ },
                onRightClick = { navController.navigate("settings") },
                logoResId = R.drawable.logo_topbar,
                logoSize = 200.dp
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->

        // If we don't want to show partial data, we can do a "loading screen" until everything is ready:
        if (isLoading) {
            // Full-screen loader
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Once isLoading == false, we show the real content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    PortfolioBalanceSection(
                        balance = totalValue,
                        percentageChange = percentChange,
                        isLoading = false // now safe to pass false
                    )
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        MarketMoversSection(
                            marketMovers = marketMovers,
                            priceUpdates = priceUpdates
                        )
                        PortfolioSection(
                            portfolioItems = cryptoViewModel.cryptoList.collectAsState().value,
                            priceUpdates = priceUpdates
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
