package com.example.traderapp.ui.screens.trade

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traderapp.ui.screens.components.PortfolioBalanceSection
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.theme.TransparentStatusBar


@Composable
fun TradeScreen(navController: NavController) {
    TransparentStatusBar()

    var selectedOption by remember { mutableStateOf("Buy") }
    var amount by remember { mutableStateOf(1000.0) }
    var cryptocurrencyAmount by remember { mutableStateOf(1.5) }
    var portfolioBalance by remember { mutableStateOf(5000.0) }

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
                .padding(24.dp)
        ) {
            // Background for buttons
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(4.dp)
                        .height(50.dp)
                        .fillMaxWidth()
                ) {
                    listOf("Buy", "Sell", "Exchange").forEachIndexed { index, option ->
                        Button(
                            onClick = { selectedOption = option },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedOption == option) Color.White else Color.Transparent
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(3.dp)
                                .then(if (index == 2) Modifier else Modifier.padding(end = 4.dp))
                        ) {
                            Text(
                                text = option,
                                color = if (selectedOption == option) Color.Black else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Show different content based on selected option
            when (selectedOption) {
                "Buy" -> {
                    // Display Buy-related information
                    Text("You pay: $$amount")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("You receive: $cryptocurrencyAmount BTC")

                    // Button to simulate the buying action
                    Button(onClick = { /* Buy action */ }) {
                        Text(text = "Buy")
                    }
                }
                "Sell" -> {
                    // Display Sell-related information
                    Text("You pay: $$amount")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("You receive: $cryptocurrencyAmount BTC")

                    // Button to simulate the selling action
                    Button(onClick = { /* Sell action */ }) {
                        Text(text = "Sell")
                    }
                }
                "Exchange" -> {
                    // Display Exchange-related information
                    Text("You Convert: $$amount")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("To: $cryptocurrencyAmount BTC")

                    // Button to simulate the exchange action
                    Button(onClick = { /* Exchange action */ }) {
                        Text(text = "Convert")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Display portfolio balance
            PortfolioBalanceSection(balance = portfolioBalance, percentageChange = 2.5)
        }
    }
}