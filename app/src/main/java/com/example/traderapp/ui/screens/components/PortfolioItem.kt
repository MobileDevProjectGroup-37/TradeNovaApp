package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto

@Composable
fun PortfolioItem(crypto: CryptoDto, currentPrice: Double) {
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
        CryptoCard(crypto, currentPrice)
    }
}
