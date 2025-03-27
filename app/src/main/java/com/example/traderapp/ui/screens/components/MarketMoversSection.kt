package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.ui.screens.components.texts.ClickableText


@Composable
fun MarketMoversSection(marketMovers: List<CryptoDto>, priceUpdates: Map<String, Double>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Market Movers",
                style = MaterialTheme.typography.titleMedium,

            )

            ClickableText(
                text = "More",
                onClick = { /* сlick */ },
                textColor = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            items(marketMovers) { crypto ->
                val currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
                MarketMoverItem(
                    crypto = crypto,
                    currentPrice = currentPrice,
                    priceChange = crypto.changePercent24h ?: 0.0
                )
            }
        }
    }
}




