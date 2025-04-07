package com.example.traderapp.ui.screens.trade

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.viewmodel.CryptoViewModel

@Composable
fun TradeScreen(navController: NavController) {
    TransparentStatusBar()

    // state for chosen tab
    var selectedOption by remember { mutableStateOf("Buy") }
    val viewModel: CryptoViewModel = hiltViewModel()

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
            // tabs to chose between Buy, Sell, Exchange
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(4.dp)
                        .height(50.dp)
                        .fillMaxWidth()
                ) {
                    listOf("Buy", "Sell", "Exchange").forEach { option ->
                        Button(
                            onClick = {
                                selectedOption = option  // renew
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedOption == option) Color.White else Color.Transparent
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .weight(2f)
                                .padding(3.dp)
                        ) {
                            Text(
                                text = option,
                                color = if (selectedOption == option) Color.Black else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (selectedOption) {
                "Buy" -> {
                    BuyTab(navController = navController)
                }
                "Sell" -> {
                    SellTab(navController = navController)
                }
                "Exchange" -> {
                    ExchangeTab(navController = navController)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
