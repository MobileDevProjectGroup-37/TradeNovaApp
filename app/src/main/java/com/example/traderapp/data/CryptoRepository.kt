package com.example.traderapp.data

import android.content.Context
import android.util.Log
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.network.CryptoApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlin.math.round

class CryptoRepository @Inject constructor(
    private val api: CryptoApi,
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()
    private val fileName = "crypto_list.json"

    suspend fun getCryptoList(): List<CryptoDto> {

        val cache = readFromCache()
        if (cache != null) {
            Log.d("CryptoRepository", "Loaded from cache")
            return cache
        }

        return try {
            val exchangeInfo = api.getExchangeInfo()
            val allPrices = api.getAllPrices()
            val ticker24hList = api.getAllTicker24hr()

            val priceMap = allPrices.associateBy { it.symbol }
            val tickerMap = ticker24hList.associateBy { it.symbol }

            val usdtSymbols = exchangeInfo.symbols
                .filter { it.quoteAsset == "USDT" && it.status == "TRADING" }

            val data = usdtSymbols.map { symbolInfo ->
                val symbol = symbolInfo.symbol
                val price = priceMap[symbol]?.price ?: ""
                val ticker = tickerMap[symbol]

                val percentChange = ticker?.priceChangePercent?.toDoubleOrNull()?.let { round(it * 100) / 100 } ?: 0.0
                val volume = ticker?.quoteVolume?.toDoubleOrNull()?.let { round(it * 100) / 100 } ?: 0.0

                CryptoDto(
                    id = symbol,
                    symbol = symbol,
                    name = "${symbolInfo.baseAsset}/USDT",
                    priceUsd = price,
                    changePercent24Hr = percentChange,
                    volumeUsd24Hr = volume
                )
            }

            saveToCache(data)
            Log.d("CryptoRepository", "Loaded from network and saved to cache")
            data

        } catch (e: Exception) {
            Log.e("CryptoRepository", "Failed to load from network: ${e.message}")
            emptyList()
        }
    }

    private fun readFromCache(): List<CryptoDto>? {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return null
            val json = file.readText()
            val type = object : TypeToken<List<CryptoDto>>() {}.type
            gson.fromJson<List<CryptoDto>>(json, type)
        } catch (e: Exception) {
            Log.e("CryptoRepository", "Error reading cache: ${e.message}")
            null
        }
    }

    private fun saveToCache(data: List<CryptoDto>) {
        try {
            val file = File(context.filesDir, fileName)
            val json = gson.toJson(data)
            file.writeText(json)
        } catch (e: Exception) {
            Log.e("CryptoRepository", "Error writing cache: ${e.message}")
        }
    }
}
