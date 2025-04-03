package com.example.traderapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.network.CryptoApi
import com.example.traderapp.data.network.RetrofitInstance
import com.example.traderapp.data.network.WebSocketClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random




// rewrite it with hilt so it could be reusable and testable
@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val api: CryptoApi,
    private val webSocketClient: WebSocketClient
) : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoDto>>(emptyList())
    val cryptoList: StateFlow<List<CryptoDto>> = _cryptoList.asStateFlow()

    val priceUpdates = webSocketClient.priceUpdates

    private val _percentageChange = MutableStateFlow(2.60)
    val percentageChange: StateFlow<Double> = _percentageChange.asStateFlow()

    private val _marketMovers = MutableStateFlow<List<CryptoDto>>(emptyList())
    val marketMovers: StateFlow<List<CryptoDto>> = _marketMovers.asStateFlow()

    init {
        loadCryptoList()
    }

    private fun loadCryptoList() {
        viewModelScope.launch {
            try {
                val response = api.getCryptoList()
                val cryptoData = response.data

                _cryptoList.value = cryptoData

                val updatedList = cryptoData.map {
                    it.copy(changePercent24Hr = Random.nextDouble(-5.0, 5.0))
                }

                _marketMovers.value = updatedList.sortedByDescending { it.changePercent24Hr }.take(5)

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



/*
class CryptoViewModel : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoDto>>(emptyList())
    val cryptoList: StateFlow<List<CryptoDto>> = _cryptoList.asStateFlow()

    private val webSocketClient = WebSocketClient()
    val priceUpdates = webSocketClient.priceUpdates
    //TODO HARDCODED, CONNECT TO DB DELETE todo after change
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
                    it.copy(changePercent24Hr = Random.nextDouble(-5.0, 5.0)) // Симуляция роста/падения
                }

                // Сортируем по изменению цены за 24ч и берем ТОП-5
                _marketMovers.value = updatedList.sortedByDescending { it.changePercent24Hr }.take(5)

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
*/