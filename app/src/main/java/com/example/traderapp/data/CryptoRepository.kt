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
        } else {
            try {

                val exchangeInfo = api.getExchangeInfo()


                val allPrices = api.getAllPrices()


                val priceMap = allPrices.associateBy(
                    keySelector = { it.symbol },
                    valueTransform = { it.price }
                )

                val data = exchangeInfo.symbols.map { symbolInfo ->
                    val symbolKey = symbolInfo.symbol
                    val priceStr = priceMap[symbolKey] ?: ""

                    CryptoDto(
                        id = symbolKey,
                        symbol = symbolKey,
                        name = "${symbolInfo.baseAsset}/${symbolInfo.quoteAsset}",
                        priceUsd = priceStr,
                        changePercent24Hr = 0.001,
                        volumeUsd24Hr = 0.0234,
                    )
                }


                saveToCache(data)
                Log.d("CryptoRepository", "Loaded from network and saved to cache")

                return data
            } catch (e: Exception) {
                Log.e("CryptoRepository", "Failed to load from network: ${e.message}")
                return emptyList()
            }
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
