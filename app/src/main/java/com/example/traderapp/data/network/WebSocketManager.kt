package com.example.traderapp.data.network

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.*
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor() {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _priceUpdates = MutableStateFlow<Map<String, Double>>(emptyMap())
    val priceUpdates: StateFlow<Map<String, Double>> = _priceUpdates.asStateFlow()


    fun connect(symbols: List<String>) {

        val limitedSymbols = symbols.take(10)

        val streamParts = limitedSymbols.map { "${it.lowercase()}@ticker" }
        val streamsParam = streamParts.joinToString("/")

        val url = "wss://stream.binance.com:9443/stream?streams=$streamsParam"
        Log.d("WebSocketClient", "Connecting to $url")

        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocketClient", "✅ WebSocket opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocketClient", "📩 Message received: $text")


                try {
                    val json = JSONObject(text)


                    if (json.has("stream") && json.has("data")) {
                        val dataObject = json.getJSONObject("data")

                        val symbol = dataObject.optString("s", "")
                        val lastPriceStr = dataObject.optString("c", "")
                        if (symbol.isNotEmpty() && lastPriceStr.isNotEmpty()) {
                            val lastPrice = lastPriceStr.toDoubleOrNull()

                            lastPrice?.let {

                                _priceUpdates.value = _priceUpdates.value.toMutableMap().apply {
                                    put(symbol, it)
                                }
                            }
                        }
                    } else {
                        Log.w("WebSocketClient", "Unknown message format: $text")
                    }
                } catch (e: Exception) {
                    Log.e("WebSocketClient", "❌ Failed to parse JSON message", e)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocketClient", "❌ WebSocket failure: ${t.message}", t)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.w("WebSocketClient", "⚠️ WebSocket closing: $code / $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("WebSocketClient", "🔌 WebSocket closed: $code / $reason")
            }
        })
    }

    fun disconnect() {
        Log.d("WebSocketClient", "🔌 Disconnecting WebSocket")
        webSocket?.close(1000, null)
    }
}
