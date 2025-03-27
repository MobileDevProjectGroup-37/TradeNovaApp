package com.example.traderapp.ui.screens.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto

@SuppressLint("DefaultLocale")
@Composable
fun MarketMoverItem(crypto: CryptoDto, currentPrice: Double, priceChange: Double) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = crypto.symbol, style = MaterialTheme.typography.bodyMedium)

            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Crypto Logo")

            // Преобразуем цену в String с форматированием
            Text(text = "$${String.format("%.2f", currentPrice)}")

            Text(
                text = if (priceChange >= 0) "+${String.format("%.2f", priceChange)}%"
                else "${String.format("%.2f", priceChange)}%",
                color = if (priceChange >= 0) Color.Green else Color.Red
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("Chart")
            }

            Text(text = "24H Vol: ${String.format("%.2f", crypto.volume24h)}")
        }
    }
}
