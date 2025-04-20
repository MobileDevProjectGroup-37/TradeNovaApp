package com.example.traderapp.viewmodel

import androidx.lifecycle.ViewModel

data class BalancePoint(val timestamp: Long, val balance: Double)

class PortfolioViewModel : ViewModel() {

    val balanceHistory = mutableListOf<BalancePoint>()

    init {
        // Генерируем 24 точки — по одной на каждый час за последние сутки
        val now = System.currentTimeMillis()
        val start = now - 24 * 60 * 60 * 1000L // 24 часа назад

        for (i in 0..24) {
            val time = start + i * 60 * 60 * 1000L // каждый час
            val balance = 1000.0 + Math.sin(i.toDouble() / 2) * 15  // тестовая волна
            balanceHistory.add(BalancePoint(timestamp = time, balance = balance))
        }
    }

    fun trackBalance(currentBalance: Double) {
        val now = System.currentTimeMillis()
        balanceHistory.add(BalancePoint(now, currentBalance))
    }
}
