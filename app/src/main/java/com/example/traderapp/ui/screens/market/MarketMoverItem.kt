package com.example.traderapp.ui.screens.market

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
    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(12.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Верхняя строка: symbol и заглушка-иконка
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = crypto.name + "/USD",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "${crypto.symbol} logo",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier
                        .size(28.dp)
                )
            }

            Text(
                text = String.format("%,.2f", currentPrice),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (priceChange >= 0) "+${String.format("%.2f", priceChange)}%" else "${String.format("%.2f", priceChange)}%",
                color = if (priceChange >= 0) Color(0xFF4CAF50) else Color.Red,
                fontWeight = FontWeight.SemiBold
            )

            // graph
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(0xFF1E1E1E), shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Chart", color = Color.Green)
            }

            // Volume label + значение
            Text(
                text = "24H Vol.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge

            )
            Text(
                text = String.format("%,.2f", crypto.volumeUsd24Hr),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
