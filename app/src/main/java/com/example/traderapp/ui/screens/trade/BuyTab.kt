package com.example.traderapp.ui.screens.trade

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.screens.components.texts.SubTitle

@Composable
fun BuyTab(amount: Double, cryptocurrencyAmount: Double) {
    SubTitle("You pay")
    AppTitle(
        text = "$$amount",
        modifier = Modifier.padding(top = 8.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(8.dp))

    Text("You receive")
    Spacer(modifier = Modifier.height(8.dp))

    QuantityCard(quantity = cryptocurrencyAmount, currency = "BTC", label = "You receive")

    Button(onClick = { /* Buy action */ }) {
        Text(text = "Buy")
    }
}