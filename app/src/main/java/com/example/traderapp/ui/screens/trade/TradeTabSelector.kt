package com.example.traderapp.ui.screens.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TradeTabSelector(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val options = listOf("Buy", "Sell", "Exchange")

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == option

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.surface
                            else
                                Color.Transparent
                        )
                        .clickable { onOptionSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 14.sp
                    )
                }

                if (index < options.lastIndex) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
    }
}
