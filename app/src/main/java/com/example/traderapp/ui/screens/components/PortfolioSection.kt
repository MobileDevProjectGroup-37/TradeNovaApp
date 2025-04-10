package com.example.traderapp.ui.screens.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.ui.screens.components.texts.ClickableText

@SuppressLint("DefaultLocale")
@Composable
fun PortfolioSection(
    portfolioItems: List<CryptoDto>,
    priceUpdates: Map<String, Double>
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
                onClick = { /* click */ },
                textColor = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        itemsToShow.forEach { crypto ->
            val currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
            val formattedPrice = "$" + String.format("%.6f", currentPrice)

            PortfolioItem(
                crypto = crypto.name,
                currentPrice = formattedPrice
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (currentIndex > 0) currentIndex -= 5
                },
                enabled = currentIndex > 0
            ) {
                Text("Back")
            }

            Button(
                onClick = {
                    if (currentIndex + 5 < portfolioItems.size) currentIndex += 5
                },
                enabled = currentIndex + 5 < portfolioItems.size
            ) {
                Text("Next")
            }
        }
    }
}
