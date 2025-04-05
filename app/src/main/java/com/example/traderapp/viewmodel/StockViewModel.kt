package com.example.traderapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.CryptoRepository
import com.example.traderapp.data.network.WebSocketClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import com.example.traderapp.BuildConfig
@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository: CryptoRepository,
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
        Log.d("CryptoViewModel", "INIT CALLED")
        loadCryptoList()
    }

    private fun loadCryptoList() {
        viewModelScope.launch {
            try {
                val cryptoData = repository.getCryptoList()
                Log.d("CryptoViewModel", "Loaded crypto list: $cryptoData")
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
