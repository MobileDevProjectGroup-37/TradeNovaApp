package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto

@Composable
fun CryptoItem(crypto: CryptoDto, currentPrice: Double) {
    // Форматируем цену, чтобы она не отображала более 5 знаков после запятой
    val formattedPrice = String.format("%.6f", currentPrice)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), shape = MaterialTheme.shapes.medium) // Рамка вокруг всей строки
            .padding(6.dp) // Паддинг внутри рамки
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),  // Паддинг между элементами внутри строки
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Название криптовалютной пары (например, BTC/USDT)
            Column(
                modifier = Modifier
                    .weight(2.4f)
                   ,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "${crypto.name}/USDT", fontWeight = FontWeight.Bold)
                Text(text = "$formattedPrice/USDT", color = MaterialTheme.colorScheme.secondary) // Здесь используется форматированная цена
            }

            // Последняя цена (Last Price)
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$formattedPrice", fontWeight = FontWeight.Bold) // Используем форматированную цену
            }

            Spacer(modifier = Modifier.width(8.dp)) // Пространство между столбиками

            // Изменение за 24 часа (24H Change)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${crypto.changePercent24h ?: 0.0}%",
                    fontWeight = FontWeight.Bold,
                    color = if (crypto.changePercent24h ?: 0.0 >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


