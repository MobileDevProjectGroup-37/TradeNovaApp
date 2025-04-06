package com.example.traderapp.ui.screens.trade

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ExchangeTab(amount: Double, cryptocurrencyAmount: Double) {
    Text("You Convert: $$amount")
    Spacer(modifier = Modifier.height(8.dp))
    Text("To: $cryptocurrencyAmount BTC")

    Text("You receive")
    Spacer(modifier = Modifier.height(8.dp))

    QuantityCard(quantity = cryptocurrencyAmount, currency = "BTC", label = "You receive")

    Button(onClick = { /* Exchange action */ }) {
        Text(text = "Convert")
    }
}