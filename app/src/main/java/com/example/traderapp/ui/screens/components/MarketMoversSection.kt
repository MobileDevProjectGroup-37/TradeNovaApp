package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.ui.screens.components.texts.SubTitle

@Composable
fun MarketMoversSection(marketMovers: List<CryptoDto>, priceUpdates: Map<String, Double>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)  // Отступы для общей композиции
    ) {
        // Заголовок с кнопкой "More" на одном уровне
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),  // Отступ снизу для красоты
            horizontalArrangement = Arrangement.SpaceBetween, // Размещаем элементы по краям
            verticalAlignment = Alignment.CenterVertically // Выровнять элементы по вертикали
        ) {
            // Заголовок
            Text(
                text = "Market Movers",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary // Цвет текста заголовка
            )
            // Кнопка More
            TextButton(
                onClick = { /* Навигация или действие при нажатии */ },
                modifier = Modifier
                   // Задний фон кнопки
                    .padding(horizontal = 12.dp, vertical = 6.dp) // Отступы внутри кнопки
            ) {
                Text("More")
            }
        }

        // LazyRow сразу под заголовком, без отступов сверху
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)  // Убираем лишний отступ
        ) {
            items(marketMovers) { crypto ->
                val currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
                MarketMoverItem(
                    crypto = crypto,
                    currentPrice = currentPrice,
                    priceChange = crypto.changePercent24h ?: 0.0
                )
            }
        }
    }
}




