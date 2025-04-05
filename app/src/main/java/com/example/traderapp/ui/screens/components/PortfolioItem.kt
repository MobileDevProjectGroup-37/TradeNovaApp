package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PortfolioItem(crypto: String, currentPrice: String) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = crypto, fontWeight = FontWeight.Bold)
                Text(text = "$$currentPrice", color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}
