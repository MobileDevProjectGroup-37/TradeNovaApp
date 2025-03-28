package com.example.traderapp.data.network

import com.example.traderapp.data.model.CryptoCoinInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("coins/markets")
    suspend fun getCoinInfo(
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") ids: String // Список криптовалют
    ): List<CryptoCoinInfo>
}

data class CryptoCoinInfo(
    val id: String,   // id криптовалюты
    val symbol: String,  // Символ криптовалюты
    val image: String // URL изображения
)
