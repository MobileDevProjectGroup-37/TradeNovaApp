package com.example.traderapp.ui.screens.trade

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.TradeViewModel

// Импорт компонента табов
import com.example.traderapp.ui.screens.trade.TradeTabSelector

@Composable
fun TradeScreen(
    navController: NavController,
    cryptoViewModel: CryptoViewModel,
    tradeViewModel: TradeViewModel,
    userSession: UserSession,
) {
    TransparentStatusBar()
    var selectedOption by remember { mutableStateOf("Buy") }

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.BACK,
                rightIconType = RightIconType.UNION,
                onBackClick = { navController.popBackStack() },
                onRightClick = { /* action */ },
                title = selectedOption
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
                .padding(8.dp)
        ) {
            // 🔁 Используем наш красивый компонент табов
            TradeTabSelector(
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (selectedOption) {
                "Buy" -> {
                    BuyTab(
                        navController = navController,
                        cryptoViewModel = cryptoViewModel,
                        tradeViewModel = tradeViewModel,
                        userSession = userSession
                    )
                }
                "Sell" -> {
                    SellTab(
                        navController = navController,
                        cryptoViewModel = cryptoViewModel,
                        tradeViewModel = tradeViewModel,
                        userSession = userSession
                    )
                }
                "Exchange" -> {
                    ExchangeTab(
                        navController = navController,
                        tradeViewModel = tradeViewModel,
                        cryptoViewModel = cryptoViewModel
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
