package com.example.traderapp.data.model

data class CryptoCoinInfo(
    val id: String,
    val image: CoinImage
)

data class CoinImage(
    val thumb: String,
    val small: String,
    val large: String // Поле, которое нам нужно
)
