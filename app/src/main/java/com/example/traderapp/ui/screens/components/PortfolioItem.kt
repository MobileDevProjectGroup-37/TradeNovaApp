package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
fun PortfolioItem(
    crypto: String,
    currentPrice: String,
    onClick: () -> Unit = {},
    selected: Boolean = false,
    showHint: Boolean = false
) {
    val backgroundColor = if (selected)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
    else
        Color.White

    val borderColor = if (selected)
        MaterialTheme.colorScheme.primary
    else
        Color.Transparent

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 6.dp)
                .background(
                    Color(0xFFD9D9D9).copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.medium
                )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                .border(1.5.dp, borderColor, MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = crypto,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$$currentPrice",
                        color = MaterialTheme.colorScheme.secondary
                    )

                    if (showHint) {
                        Text(
                            text = if (selected) "✓ Selected" else "Tap to buy",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
