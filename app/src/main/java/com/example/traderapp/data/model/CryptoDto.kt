package com.example.traderapp.data.model

data class CryptoDto(
    val id: String,                  // "BTCUSDT"
    val symbol: String,              // "BTCUSDT"
    val name: String,                // "BTC/USDT"
    val priceUsd: String,            // "27600.25"
    val changePercent24Hr: Double,   // 2.56
    val volumeUsd24Hr: Double,
  //  val balance: Double  // 123456789.0
)
