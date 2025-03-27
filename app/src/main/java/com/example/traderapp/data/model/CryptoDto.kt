package com.example.traderapp.data.model

data class CryptoDto(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val priceUsd: String,
    val volume24h: Double,
    val changePercent24h: Double? = null
)
