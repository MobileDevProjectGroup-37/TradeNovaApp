package com.example.traderapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.CryptoDto

@Composable
fun PortfolioSection(portfolioItems: List<CryptoDto>, priceUpdates: Map<String, Double>) {
    val maxItemsToShow = 5 // Максимальное количество элементов, которое отображаем сразу
    var currentIndex by remember { mutableStateOf(0) } // Индекс текущих элементов

    // Функция для получения видимых элементов с учетом currentIndex
    val visibleItems = portfolioItems.drop(currentIndex).take(maxItemsToShow)

    // Функции для перехода вперед и назад
    fun goToNext() {
        if (currentIndex + maxItemsToShow < portfolioItems.size) {
            currentIndex += maxItemsToShow
        }
    }

    fun goToPrevious() {
        if (currentIndex - maxItemsToShow >= 0) {
            currentIndex -= maxItemsToShow
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)  // Отступы для общей композиции
    ) {
        // Заголовок с кнопкой More на одном уровне
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),  // Отступ снизу для красоты
            horizontalArrangement = Arrangement.SpaceBetween, // Размещаем элементы по краям
            verticalAlignment = Alignment.CenterVertically // Выровнять элементы по вертикали
        ) {
            // Заголовок
            Text(
                text = "Portfolio",
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

        // Отображаем только видимые элементы
        Column(modifier = Modifier.fillMaxWidth()) {
            visibleItems.forEach { crypto ->
                val currentPrice = priceUpdates[crypto.id] ?: crypto.priceUsd?.toDoubleOrNull() ?: 0.0
                PortfolioItem(
                    crypto = crypto,
                    currentPrice = currentPrice
                )
            }
        }

        // Кнопки навигации (Previous/Next) показываем только когда нужно
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Кнопка для возврата на предыдущие элементы
            if (currentIndex > 0) {
                TextButton(
                    onClick = { goToPrevious() },
                    modifier = Modifier.fillMaxWidth(0.45f)
                ) {
                    Text("Previous")
                }
            }

            // Кнопка для перехода к следующей странице
            if (currentIndex + maxItemsToShow < portfolioItems.size) {
                TextButton(
                    onClick = { goToNext() },
                    modifier = Modifier.fillMaxWidth(0.45f)
                ) {
                    Text("Next")
                }
            }
        }
    }
}
