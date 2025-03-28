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
    priceUpdates: List<Double>
) {
    var currentIndex by remember { mutableStateOf(0) }

    val itemsToShow = portfolioItems.drop(currentIndex).take(5)

    Column(modifier = Modifier.padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Portfolio",
                style = MaterialTheme.typography.titleMedium
            )
            ClickableText(
                text = "More",
                onClick = { /* сlick */ },
                textColor = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        itemsToShow.forEachIndexed { index, crypto ->
            PortfolioItem(crypto = crypto, currentPrice = priceUpdates.getOrNull(index + currentIndex) ?: 0.0)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex -= 5
                    }
                },
                enabled = currentIndex > 0
            ) {
                Text("Back")
            }

            Button(
                onClick = {
                    if (currentIndex + 5 < portfolioItems.size) {
                        currentIndex += 5
                    }
                },
                enabled = currentIndex + 5 < portfolioItems.size
            ) {
                Text("Next")
            }
        }
    }
}
