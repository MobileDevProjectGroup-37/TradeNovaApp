package com.example.traderapp.data.model

data class CryptoDto(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val priceUsd: String,
    val volumeUsd24Hr: Double,
    val changePercent24Hr: Double? = null
)
