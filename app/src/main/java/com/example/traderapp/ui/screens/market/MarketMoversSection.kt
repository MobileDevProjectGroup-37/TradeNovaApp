package com.example.traderapp.ui.screens.market

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.data.CryptoRepository
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.ui.screens.components.texts.ClickableText

@Composable
fun MarketMoversSection(
    marketMovers: List<CryptoDto>,
    priceUpdates: Map<String, Double>,
    repository: CryptoRepository
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Market Movers",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            ClickableText(
                text = "More",
                onClick = { showDialog = true },
                textColor = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {
            items(marketMovers) { crypto ->
                val currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
                var chartData by remember { mutableStateOf<List<Double>>(emptyList()) }

                LaunchedEffect(crypto.symbol) {
                    chartData = repository.getMiniChart(crypto.symbol)
                }

                MarketMoverItem(
                    crypto = crypto,
                    currentPrice = currentPrice,
                    priceChange = crypto.changePercent24Hr ?: 0.0,
                    chartData = chartData
                )
            }
        }
    }

    // AlertDialog at the end of Composable
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Coming Soon!") },
            text = { Text("This feature is currently under development.") }
        )
    }
}
