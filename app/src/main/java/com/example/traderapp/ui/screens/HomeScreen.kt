package com.example.traderapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.MarketMoversSection
import com.example.traderapp.ui.screens.components.PortfolioBalanceSection
import com.example.traderapp.ui.screens.components.PortfolioSection
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.viewmodel.CryptoViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: CryptoViewModel = viewModel()) {
    val balance by viewModel.balance.collectAsState()
    val percentageChange by viewModel.percentageChange.collectAsState()
    val marketMovers by viewModel.marketMovers.collectAsState()
    val portfolioItems by viewModel.cryptoList.collectAsState()
    val priceUpdates by viewModel.priceUpdates.collectAsState()

    TransparentStatusBar()

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.PROFILE,
                rightIconType = RightIconType.SETTINGS,
                onBackClick = { /* action */ },
                onRightClick = { /* action */ },
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
                PortfolioBalanceSection(balance = balance, percentageChange = percentageChange)
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    MarketMoversSection(marketMovers = marketMovers, priceUpdates = priceUpdates)
                    PortfolioSection(portfolioItems = portfolioItems, priceUpdates = priceUpdates.values.toList())

                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
