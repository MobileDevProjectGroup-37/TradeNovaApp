package com.example.traderapp.data.model


data class CryptoDto(
    val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: String,
    val changePercent24Hr: Double,
    val volumeUsd24Hr: Double
)

