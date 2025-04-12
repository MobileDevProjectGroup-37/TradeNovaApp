// PortfolioItem.kt
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
    amount: String? = null,
    usdValue: String? = null,
    onClick: () -> Unit = {},
    selected: Boolean = false,
    showHint: Boolean = false,
    hintText: String = ""
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
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // 🔹 Название криптовалюты
                Text(text = crypto, fontWeight = FontWeight.Bold)

                // 🔹 Кол-во криптовалюты
                amount?.let {
                    Text("Owned: $it")
                }

                // 🔹 Суммарная стоимость в $
                usdValue?.let {
                    Text("Total value: \$$it")
                }

                // 🔹 Цена за 1 единицу + hint
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1 $crypto ≈ \$$currentPrice",
                        color = MaterialTheme.colorScheme.secondary
                    )

                    if (showHint) {
                        Text(
                            text = if (selected) "✓ Selected" else hintText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
