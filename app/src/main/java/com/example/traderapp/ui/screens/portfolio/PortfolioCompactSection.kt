package com.example.traderapp.ui.screens.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PortfolioCompactSection(
    data: List<PortfolioCompactData> // name, price, changePercent
) {
    var currentIndex by remember { mutableStateOf(0) }
    val visibleItems = data.drop(currentIndex).take(5)

    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp)) {
        Text(
            text = "My portfolio",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        visibleItems.forEach { item ->
            PortfolioCompactItem(
                name = item.name,
                price = item.price,
                changePercent = item.changePercent
            )
        }

        if (data.size > 5) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { if (currentIndex >= 3) currentIndex -= 3 },
                    enabled = currentIndex > 0
                ) {
                    Text("Back")
                }
                Button(
                    onClick = {
                        if (currentIndex + 5 < data.size) currentIndex += 3
                    },
                    enabled = currentIndex + 5 < data.size
                ) {
                    Text("Next")
                }
            }
        }
    }
}

data class PortfolioCompactData(
    val name: String,
    val price: Double,
    val changePercent: Double
)
