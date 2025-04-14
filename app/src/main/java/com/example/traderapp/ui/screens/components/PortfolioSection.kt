package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.ui.screens.components.texts.ClickableText

@Composable
fun PortfolioSection(
    portfolioItems: List<CryptoDto>,
    priceUpdates: Map<String, Double>,
    userAssets: Map<String, Double>
) {
    val userPortfolio = portfolioItems.filter { userAssets[it.id] ?: 0.0 > 0.0 }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My portfolio",
                style = MaterialTheme.typography.titleMedium
            )
            ClickableText(
                text = "More",
                onClick = { /* click */ },
                textColor = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        userPortfolio.forEach { crypto ->
            val price = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
            val amount = userAssets[crypto.id] ?: 0.0
            val valueUsd = amount * price

            PortfolioItem(
                crypto = crypto.name,
                currentPrice = String.format("%.2f", price),
                amount = String.format("%.4f", amount),
                usdValue = String.format("%.2f", valueUsd),
                changePercent = crypto.changePercent24Hr,
                compact = true
            )
        }
    }
}
