package com.example.traderapp.viewmodel

import androidx.lifecycle.ViewModel

data class BalancePoint(val timestamp: Long, val balance: Double)

class PortfolioViewModel : ViewModel() {

    val balanceHistory = mutableListOf<BalancePoint>()

    init {

        val now = System.currentTimeMillis()
        val start = now - 24 * 60 * 60 * 1000L

        for (i in 0..24) {
            val time = start + i * 60 * 60 * 1000L
            val balance = 1000.0 + Math.sin(i.toDouble() / 2) * 15
            balanceHistory.add(BalancePoint(timestamp = time, balance = balance))
        }
    }

    fun trackBalance(currentBalance: Double) {
        val now = System.currentTimeMillis()
        balanceHistory.add(BalancePoint(now, currentBalance))
    }
}
