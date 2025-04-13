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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto


@SuppressLint("DefaultLocale")
@Composable
fun MarketMoverItem(crypto: CryptoDto, currentPrice: Double, priceChange: Double) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(140.dp)
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 6.dp)
                .background(Color(4287085311).copy(alpha = 0.2f), shape = MaterialTheme.shapes.medium)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, shape = MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors( containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = crypto.symbol, style = MaterialTheme.typography.bodyMedium)

                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Crypto Logo")

                Text(text = "$${String.format("%.2f", currentPrice)}")

                Text(
                    text = if (priceChange >= 0) "+${String.format("%.2f", priceChange)}%"
                    else "${String.format("%.2f", priceChange)}%",
                    color = if (priceChange >= 0) MaterialTheme.colorScheme.primary else Color.Red,
                    fontWeight = FontWeight.Bold
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

                Text(text = "24H Vol: ${String.format("%.2f", crypto.volumeUsd24Hr)}")
            }
        }
    }
}
