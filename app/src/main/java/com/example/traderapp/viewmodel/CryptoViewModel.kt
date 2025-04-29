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

@HiltViewModel
class CryptoViewModel @Inject constructor(
    val repository: CryptoRepository, // Injected repository to fetch crypto data via REST API.
    private val webSocketClient: WebSocketClient // Injected WebSocket client to receive live price updates.
) : ViewModel() {


    private val _cryptoList = MutableStateFlow<List<CryptoDto>>(emptyList()) // Internal StateFlow holding the list of available cryptos.
    val cryptoList: StateFlow<List<CryptoDto>> = _cryptoList.asStateFlow() // Public read-only StateFlow exposed to the UI.

    // priceUpdates holds live price changes separately to keep static cryptoList immutable and stable for Compose.

    val priceUpdates = webSocketClient.priceUpdates // Live prices directly streamed from WebSocketClient.

    private val _marketMovers = MutableStateFlow<List<CryptoDto>>(emptyList()) // Internal StateFlow for tracking top movers (by 24h % change).
    val marketMovers: StateFlow<List<CryptoDto>> = _marketMovers.asStateFlow() // Public read-only flow for UI to observe top movers.

    init {
        Log.d("CryptoViewModel", "INIT CALLED") // Log to check when the ViewModel is initialized.
        loadCryptoList() // Immediately start loading crypto data on ViewModel creation.
    }

    private fun loadCryptoList() {
        viewModelScope.launch { // Launch a coroutine tied to ViewModel lifecycle.
            try {
                val cryptoData = repository.getCryptoList() // Fetch the list of cryptos via REST API.
                Log.d("CryptoViewModel", "Loaded crypto list: $cryptoData")
                _cryptoList.value = cryptoData // Update StateFlow with the static list of cryptos.

                _marketMovers.value = cryptoData
                    .sortedByDescending { it.changePercent24Hr } // Sort cryptos by 24h price change descending.
                    .take(5) // Take the top 5 movers.

                val symbols = cryptoData.map { it.id.lowercase() } // Extract and lowercase all symbol IDs.
                webSocketClient.connect(symbols) // Start WebSocket subscription for real-time price updates.
            } catch (e: Exception) {
                Log.e("CryptoViewModel", "Error loading crypto list: ${e.message}") // Log any error that happens during loading.
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.disconnect() // Properly disconnect the WebSocket when the ViewModel is destroyed.
    }
}
