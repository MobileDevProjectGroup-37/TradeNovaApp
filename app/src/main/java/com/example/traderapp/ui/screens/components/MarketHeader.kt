package com.example.traderapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MarketHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pair\nUSDT",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(2.6f)
                .align(Alignment.CenterVertically)
        )

        Text(
            text = "Last Price",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1.8f)
                .align(Alignment.Bottom)
        )

        Text(
            text = "24H\nChange",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Bottom)
        )
    }
}
