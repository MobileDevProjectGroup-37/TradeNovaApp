package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto

@Composable
fun CryptoItem(crypto: CryptoDto, currentPrice: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = crypto.name, fontWeight = FontWeight.Bold)
            Text(text = "$currentPrice USDT", color = MaterialTheme.colorScheme.secondary)
        }
    }
}
