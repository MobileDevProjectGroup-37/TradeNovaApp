package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.ui.screens.components.texts.ClickableText

@Composable
fun PortfolioSection(portfolioItems: List<CryptoDto>, priceUpdates: Map<String, Double>) {
    val maxItemsToShow = 5
    var currentIndex by remember { mutableIntStateOf(0) }

    val visibleItems = portfolioItems.drop(currentIndex).take(maxItemsToShow)


    fun goToNext() {
        if (currentIndex + maxItemsToShow < portfolioItems.size) {
            currentIndex += maxItemsToShow
        }
    }

    fun goToPrevious() {
        if (currentIndex - maxItemsToShow >= 0) {
            currentIndex -= maxItemsToShow
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Portfolio",
                style = MaterialTheme.typography.titleMedium,
            )

            ClickableText(
                text = "More",
                onClick = { /* сlick */ },
                textColor = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            visibleItems.forEach { crypto ->
                val currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
                PortfolioItem(
                    crypto = crypto,
                    currentPrice = currentPrice
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (currentIndex > 0) {
                TextButton(
                    onClick = { goToPrevious() },
                    modifier = Modifier.fillMaxWidth(0.45f),

                ) {
                    Text("Previous")
                }
            }

            if (currentIndex + maxItemsToShow < portfolioItems.size) {
                TextButton(
                    onClick = { goToNext() },
                    modifier = Modifier.fillMaxWidth(0.45f)
                ) {
                    Text("Next")
                }
            }
        }
    }
}
