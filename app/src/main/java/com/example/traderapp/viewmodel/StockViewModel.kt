package com.example.traderapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.network.RetrofitInstance
import com.example.traderapp.data.network.WebSocketClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoViewModel : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoDto>>(emptyList())
    val cryptoList: StateFlow<List<CryptoDto>> = _cryptoList.asStateFlow()

    private val webSocketClient = WebSocketClient()
    val priceUpdates = webSocketClient.priceUpdates
    init {
        loadCryptoList()
    }

    private fun loadCryptoList() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCryptoList()
                _cryptoList.value = response.data //

                val symbols = response.data.map { it.id.lowercase() }
                webSocketClient.connect(symbols) //

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

