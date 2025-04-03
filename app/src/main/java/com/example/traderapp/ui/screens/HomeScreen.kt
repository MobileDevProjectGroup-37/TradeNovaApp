package com.example.traderapp.ui.screens

import android.util.Log
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.test.core.app.ActivityScenario.launch
import com.example.traderapp.R
import com.example.traderapp.data.network.RetrofitInstance
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.ui.screens.components.MarketMoversSection
import com.example.traderapp.ui.screens.components.PortfolioBalanceSection
import com.example.traderapp.ui.screens.components.PortfolioSection
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.viewmodel.CryptoViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: CryptoViewModel,
    userSession: UserSession
) {
    Log.d("HOME", "HomeScreen Composable loaded")

    LaunchedEffect(Unit) {
        userSession.loadUserData()
    }

    val balance by userSession.userData.collectAsState()
    val percentageChange by viewModel.percentageChange.collectAsState()
    val marketMovers by viewModel.marketMovers.collectAsState()
    val portfolioItems by viewModel.cryptoList.collectAsState()
    val priceUpdates by viewModel.priceUpdates.collectAsState()

    TransparentStatusBar()
    Log.d("CRYPTO_LIST", "Items: ${portfolioItems.size}")
    Log.d("MARKET_MOVERS", "Items: ${marketMovers.size}")
    Log.d("PRICE_UPDATES", "Items: ${priceUpdates.size}")
    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.PROFILE,
                rightIconType = RightIconType.SETTINGS,
                onBackClick = { /* action */ },
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
                balance?.balance?.let {
                    PortfolioBalanceSection(
                        balance = it,
                        percentageChange = percentageChange,
                        isLoading = balance == null
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MarketMoversSection(
                        marketMovers = marketMovers,
                        priceUpdates = priceUpdates
                    )
                    PortfolioSection(
                        portfolioItems = portfolioItems,
                        priceUpdates = priceUpdates.values.toList()
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}