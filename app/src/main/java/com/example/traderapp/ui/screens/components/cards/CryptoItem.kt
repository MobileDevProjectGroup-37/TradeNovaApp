package com.example.traderapp.ui.screens.components.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto

@SuppressLint("DefaultLocale")

@Composable
fun CryptoItem(crypto: CryptoDto, currentPrice: Double) {

    val formattedPrice = String.format("%.4f", currentPrice)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), shape = MaterialTheme.shapes.medium)
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "${crypto.name}", fontWeight = FontWeight.Bold)
                // Text(text = "$formattedPrice/USDT", color = MaterialTheme.colorScheme.secondary)
            }


            Column(
                modifier = Modifier
                    .weight(1.6f)
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = formattedPrice, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 24H Change
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val rawChange = crypto.changePercent24Hr ?: 0.0
                val formattedChange = String.format("%.2f", rawChange)
                val sign = if (rawChange >= 0) "+" else ""
                val color = if (rawChange >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

                Text(
                    text = "$sign$formattedChange%",
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}
