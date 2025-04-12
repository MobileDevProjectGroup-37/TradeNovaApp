package com.example.traderapp.data.model

data class TradeRecord(
    val type: String = "",
    val assetId: String = "",
    val assetName: String = "",
    val price: Double = 0.0,
    val quantity: Double = 0.0,
    val totalValue: Double = 0.0,
    val timestamp: Long = 0L
)

