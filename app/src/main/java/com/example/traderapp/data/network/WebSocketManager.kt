package com.example.traderapp.data.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class WebSocketClient {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _priceUpdates = MutableStateFlow<Map<String, Double>>(emptyMap())
    val priceUpdates: StateFlow<Map<String, Double>> = _priceUpdates.asStateFlow()
    fun connect(symbols: List<String>) {
        val assets = symbols.joinToString(",") { it.lowercase() }
        val request = Request.Builder()
            .url("wss://ws.coincap.io/prices?assets=$assets")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = JSONObject(text)

                _priceUpdates.value = _priceUpdates.value.toMutableMap().apply {
                    for (key in json.keys()) {
                        put(key, json.getDouble(key)) // Get prices in JSON { "bitcoin": 42000, "ethereum": 2800 }
                    }
                }
            }
        })
    }



    fun disconnect() {
        webSocket?.close(1000, null)
    }
}
