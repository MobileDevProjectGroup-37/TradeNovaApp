package com.example.traderapp.data

import android.content.Context
import android.util.Log
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.network.CryptoApi
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlin.math.round

// Wrapper class for cached data with a timestamp
data class CryptoCacheData(
    val timestamp: Long,
    val data: List<CryptoDto>
)
//  This is a simple wrapper class for our cache. It saves both the timestamp and the actual list of cryptos.

class CryptoRepository @Inject constructor(
    private val api: CryptoApi,
    @ApplicationContext private val context: Context
) {
    // initialize basic tools: Gson for JSON conversion, the cache file name, and the cache expiration time (15 minutes).
    private val gson = Gson()
    private val fileName = "crypto_list.json"
    private val cacheValidityMillis = 15 * 60 * 1000 // 15 min


    suspend fun getCryptoList(): List<CryptoDto> {
        //read the cache and get the current time.
        val cached = readFromCache()
        val now = System.currentTimeMillis()


        if (cached != null && (now - cached.timestamp) < cacheValidityMillis) {
            Log.d("CryptoRepository", "Loaded from cache (still valid)")
            return cached.data
            // If the cache exists and is still fresh, we use it and return immediately.
        }

        return try {
            // If the cache is missing or outdated, we load fresh data from Binance API.
            val exchangeInfo = api.getExchangeInfo()
            val allPrices = api.getAllPrices()
            val ticker24hList = api.getAllTicker24hr()

            // organize the API data into maps for quick lookup by symbol.
            val priceMap = allPrices.associateBy { it.symbol }
            val tickerMap = ticker24hList.associateBy { it.symbol }

            //filter only USDT trading pairs because we focus on stablecoin markets.
            val usdtSymbols = exchangeInfo.symbols
                .filter { it.quoteAsset == "USDT" && it.status == "TRADING" }

            //We build a clean list of CryptoDto objects, extracting prices, volumes, and changes.
            val data = usdtSymbols.map { symbolInfo ->
                val symbol = symbolInfo.symbol
                val price = priceMap[symbol]?.price ?: ""
                val ticker = tickerMap[symbol]

                val percentChange = ticker?.priceChangePercent?.toDoubleOrNull()
                    ?.let { round(it * 100) / 100 } ?: 0.0

                val volume = ticker?.quoteVolume?.toDoubleOrNull()
                    ?.let { round(it * 100) / 100 } ?: 0.0

                CryptoDto(
                    id = symbol,
                    symbol = symbol,
                    name = "${symbolInfo.baseAsset}",
                    priceUsd = price,
                    changePercent24Hr = percentChange,
                    volumeUsd24Hr = volume,
                )
            }


            saveToCache(data)
            Log.d("CryptoRepository", "Loaded from network and saved to cache")
            return data
            //After fetching, we save the new data to cache for next time.
        } catch (e: Exception) {
            Log.e("CryptoRepository", "Failed to load from network: ${e.message}")
            cached?.data ?: emptyList()
            //If API call fails, we fall back to cache if we have one, or return an empty list.
        }
    }

    private fun readFromCache(): CryptoCacheData? {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return null
            val json = file.readText()
            gson.fromJson(json, CryptoCacheData::class.java)
            //This method tries to read the cache file and deserialize it back to CryptoCacheData.
        } catch (e: Exception) {
            Log.e("CryptoRepository", "Error reading cache: ${e.message}")
            null
            //If reading or parsing fails, we just return null.
        }
    }

    private fun saveToCache(data: List<CryptoDto>) {
        try {
            val file = File(context.filesDir, fileName)
            val cacheData = CryptoCacheData(
                timestamp = System.currentTimeMillis(),
                data = data
            )
            val json = gson.toJson(cacheData)
            file.writeText(json)
            // This method takes the list of crypto data, wraps it with a timestamp, converts to JSON, and saves it into the file.
        } catch (e: Exception) {
            Log.e("CryptoRepository", "Error writing cache: ${e.message}")
            // If saving fails, we log the error but the app keeps running.
        }
    }

    
    suspend fun getMiniChart(symbol: String): List<Double> {
        return try {
            val klines = api.getKlines(symbol = symbol, interval = "1h", limit = 10)
            klines.mapNotNull {
                (it[4] as? String)?.toDoubleOrNull()
            }
            // Bonus method — fetches last 10 hourly closing prices for a crypto to draw a mini-chart.
        } catch (e: Exception) {
            Log.e("CryptoRepository", "Failed to load chart for $symbol: ${e.message}")
            emptyList()
            // If fetching mini-chart fails, return an empty list.
        }
    }
}
