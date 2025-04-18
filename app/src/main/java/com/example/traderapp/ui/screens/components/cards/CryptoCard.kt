package com.example.traderapp.ui.screens.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto

@Composable
fun CryptoCard(crypto: CryptoDto, currentPrice: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = crypto.name, fontWeight = FontWeight.Bold)
            Text(text = "$$currentPrice", color = MaterialTheme.colorScheme.secondary)
        }
    }
}