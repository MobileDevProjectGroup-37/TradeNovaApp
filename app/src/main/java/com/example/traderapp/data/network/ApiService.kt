package com.example.traderapp.data.network

import retrofit2.http.GET

interface CryptoApi {

    @GET("api/v3/exchangeInfo")
    suspend fun getExchangeInfo(): ExchangeInfoResponse

    @GET("api/v3/ticker/price")
    suspend fun getAllPrices(): List<PriceResponse>

    @GET("api/v3/ticker/24hr")
    suspend fun getAllTicker24hr(): List<Ticker24hrResponse>

    @GET("api/v3/klines")
    suspend fun getKlines(
        @retrofit2.http.Query("symbol") symbol: String,
        @retrofit2.http.Query("interval") interval: String = "1h",
        @retrofit2.http.Query("limit") limit: Int = 10
    ): List<List<Any>>

}

data class ExchangeInfoResponse(
    val timezone: String,
    val serverTime: Long,
    val symbols: List<SymbolInfo>
)

data class SymbolInfo(
    val symbol: String,
    val status: String,
    val baseAsset: String,
    val quoteAsset: String
)

data class PriceResponse(
    val symbol: String,
    val price: String
)

data class Ticker24hrResponse(
    val symbol: String,
    val lastPrice: String,
    val priceChangePercent: String,
    val quoteVolume: String
)
