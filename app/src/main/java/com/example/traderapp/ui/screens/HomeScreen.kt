package com.example.traderapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
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
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    cryptoViewModel: CryptoViewModel,
    tradeViewModel: TradeViewModel,
    userSession: UserSession
) {
    // Observe values from TradeViewModel
    val userBalance by tradeViewModel.userBalance.collectAsState()
    val portfolioValue by tradeViewModel.portfolioValue.collectAsState()
    val totalValue by tradeViewModel.totalValue.collectAsState()

    // Observe prices from CryptoViewModel
    val priceUpdates by cryptoViewModel.priceUpdates.collectAsState()
    val marketMovers by cryptoViewModel.marketMovers.collectAsState()

    // One-time effects, e.g. load data
    LaunchedEffect(Unit) {
        userSession.loadUserData()
        delay(300) // small delay, so userSession has data
        tradeViewModel.loadUserAssets()
        tradeViewModel.observePriceUpdates(cryptoViewModel.priceUpdates)
    }

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                // Show totalValue as "balance" on HomeScreen
                PortfolioBalanceSection(
                    balance = totalValue,
                    // If у вас есть отдельный percentageChange — берите из viewModel
                    percentageChange = cryptoViewModel.percentageChange.collectAsState().value,
                    isLoading = false // или использовать логику загрузки
                )
            }

            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    MarketMoversSection(
                        marketMovers = marketMovers,
                        priceUpdates = priceUpdates
                    )

                    // Если хотите показать "портфель" как список активов юзера:
                    // Текущий код PortfolioSection принимает cryptoList,
                    // но нужно как-то учитывать количество монет, которые есть у юзера.
                    // Либо расширить PortfolioSection, чтобы она принимала userAssets
                    // и сама умножала на цену.
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
