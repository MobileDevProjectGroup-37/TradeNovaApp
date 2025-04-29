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

    private val client = OkHttpClient() // Create an OkHttpClient instance for handling WebSocket connections.
    private var webSocket: WebSocket? = null // This will hold the active WebSocket connection object.

    private val _priceUpdates = MutableStateFlow<Map<String, Double>>(emptyMap()) // Internal MutableStateFlow to store live price updates.
    val priceUpdates: StateFlow<Map<String, Double>> = _priceUpdates.asStateFlow() // Public read-only StateFlow exposed to ViewModel/UI.

    fun connect(symbols: List<String>) {

        val limitedSymbols = symbols.take(10) // Limit the number of subscribed symbols to 10 to avoid overloading the connection.
        val streamParts = limitedSymbols.map { "${it.lowercase()}@ticker" } // Format each symbol for Binance WebSocket subscription.
        val streamsParam = streamParts.joinToString("/") // Join all formatted streams with slashes to build the URL.

        val url = "wss://stream.binance.com:9443/stream?streams=$streamsParam" // Create the full WebSocket URL.
        Log.d("WebSocketClient", "Connecting to $url")

        val request = Request.Builder()
            .url(url) // Set the WebSocket request URL.
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocketClient", "WebSocket connection opened.")
                // Called when the WebSocket connection is successfully opened.
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocketClient", "Message received: $text")
                // Called when a message is received from the WebSocket.

                try {
                    val json = JSONObject(text) // Parse the incoming message text as a JSON object.

                    if (json.has("stream") && json.has("data")) { // Check if the message contains expected fields.
                        val dataObject = json.getJSONObject("data") // Extract the "data" JSON object.

                        val symbol = dataObject.optString("s", "") // Get the symbol string.
                        val lastPriceStr = dataObject.optString("c", "") // Get the last traded price as a string.

                        if (symbol.isNotEmpty() && lastPriceStr.isNotEmpty()) { // Ensure both symbol and price are not empty.
                            val lastPrice = lastPriceStr.toDoubleOrNull() // Safely convert the price string to a Double.

                            lastPrice?.let {
                                _priceUpdates.value = _priceUpdates.value.toMutableMap().apply {
                                    put(symbol, it) // Update the price for the symbol in the StateFlow map.
                                }
                            }
                        }
                    } else {
                        // Handle unknown message formats if necessary.
                    }
                } catch (e: Exception) {
                    // Handle JSON parsing errors.
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // Called when the WebSocket connection fails due to an error.
                // Reconnection logic can be implemented here if needed.
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // Called when the WebSocket is about to close.
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                // Called when the WebSocket connection has been closed.
            }
        })
    }

    fun disconnect() {
        // Manually close the WebSocket connection with normal closure code 1000.
        webSocket?.close(1000, null)
    }
}
