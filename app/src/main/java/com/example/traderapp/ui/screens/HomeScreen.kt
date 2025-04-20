package com.example.traderapp.ui.screens

import SetStatusBarColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.ui.screens.components.bars.*
import com.example.traderapp.ui.screens.market.MarketMoversSection
import com.example.traderapp.ui.screens.portfolio.PortfolioBalanceSection
import com.example.traderapp.ui.screens.portfolio.PortfolioCompactData
import com.example.traderapp.ui.screens.portfolio.PortfolioCompactSection
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.TradeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    cryptoViewModel: CryptoViewModel,
    tradeViewModel: TradeViewModel,
    userSession: UserSession
) {
    val userBalance by tradeViewModel.userBalance.collectAsState()
    val portfolioValue by tradeViewModel.portfolioValue.collectAsState()
    val totalValue by tradeViewModel.totalValue.collectAsState()
    val percentChange by tradeViewModel.percentageChange.collectAsState()
    val isLoading by tradeViewModel.isLoading.collectAsState()
    val userAssets by tradeViewModel.userAssets.collectAsState()

    val priceUpdates by cryptoViewModel.priceUpdates.collectAsState()
    val marketMovers by cryptoViewModel.marketMovers.collectAsState()
    val cryptoList by cryptoViewModel.cryptoList.collectAsState()

    val repository = cryptoViewModel.repository

    SetStatusBarColor()

    LaunchedEffect(Unit) {
        tradeViewModel.loadInitialData(cryptoViewModel)
    }

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.PROFILE,
                rightIconType = RightIconType.SETTINGS,
                onBackClick = { /* back */ },
                onRightClick = { navController.navigate("settings") },
                logoResId = R.drawable.logo_topbar,
                logoSize = 200.dp
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        if (isLoading) {
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
                        isLoading = false
                    )
                }

                item {
                    MarketMoversSection(
                        marketMovers = marketMovers,
                        priceUpdates = priceUpdates,
                        repository = repository
                    )
                }

                item {
                    PortfolioCompactSection(
                        data = cryptoList
                            .filter { (userAssets[it.id] ?: 0.0) > 0.0 }
                            .map {
                                PortfolioCompactData(
                                    name = it.name,
                                    price = priceUpdates[it.id] ?: it.priceUsd.toDoubleOrNull() ?: 0.0,
                                    changePercent = it.changePercent24Hr
                                )
                            }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
