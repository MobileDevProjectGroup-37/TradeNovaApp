package com.example.traderapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.network.RetrofitInstance
import com.example.traderapp.data.network.WebSocketClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class CryptoViewModel : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoDto>>(emptyList())
    val cryptoList: StateFlow<List<CryptoDto>> = _cryptoList.asStateFlow()

    private val webSocketClient = WebSocketClient()
    val priceUpdates = webSocketClient.priceUpdates

    // 🔹 Portfolio Balance (Можно заменить на реальные данные)
    private val _balance = MutableStateFlow(2760.23)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    // 🔹 Изменение баланса в %
    private val _percentageChange = MutableStateFlow(2.60)
    val percentageChange: StateFlow<Double> = _percentageChange.asStateFlow()

    // 🔹 Market Movers (ТОП-5 по изменению цены за 24ч)
    private val _marketMovers = MutableStateFlow<List<CryptoDto>>(emptyList())
    val marketMovers: StateFlow<List<CryptoDto>> = _marketMovers.asStateFlow()

    init {
        loadCryptoList()
    }

    private fun loadCryptoList() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCryptoList()
                val cryptoData = response.data

                _cryptoList.value = cryptoData

                val updatedList = cryptoData.map {
                    it.copy(changePercent24h = Random.nextDouble(-5.0, 5.0)) // Симуляция роста/падения
                }

                // Сортируем по изменению цены за 24ч и берем ТОП-5
                _marketMovers.value = updatedList.sortedByDescending { it.changePercent24h }.take(5)

                // Подключаем WebSocket для обновления цен
                val symbols = cryptoData.map { it.id.lowercase() }
                webSocketClient.connect(symbols)

            } catch (e: Exception) {
                Log.e("CryptoViewModel", "Error loading crypto list: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.disconnect()
    }
}
