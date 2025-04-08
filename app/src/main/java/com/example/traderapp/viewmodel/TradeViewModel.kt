package com.example.traderapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.model.TradeRecord
import com.example.traderapp.data.model.TradeType
import com.example.traderapp.data.network.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val userSession: UserSession
) : ViewModel() {

    // Holds any trade error message
    private val _tradeError = MutableStateFlow<String?>(null)
    val tradeError: StateFlow<String?> = _tradeError.asStateFlow()

    // User's fiat balance in dollars
    private val _userBalance = MutableStateFlow(0.0)
    val userBalance: StateFlow<Double> = _userBalance.asStateFlow()

    // Map of assetId -> quantity
    private val _userAssets = MutableStateFlow<Map<String, Double>>(emptyMap())
    val userAssets: StateFlow<Map<String, Double>> = _userAssets.asStateFlow()

    // Calculated portfolio value (sum of each asset quantity * current price)
    private val _portfolioValue = MutableStateFlow(0.0)
    val portfolioValue: StateFlow<Double> = _portfolioValue.asStateFlow()

    // Total value = userBalance + portfolioValue
    private val _totalValue = MutableStateFlow(0.0)
    val totalValue: StateFlow<Double> = _totalValue.asStateFlow()

    // Price updates from CryptoViewModel (assetId -> currentPrice)
    // We'll collect them in observePriceUpdates(...) below
    private var priceUpdates: Map<String, Double> = emptyMap()

    private val _percentageChange = MutableStateFlow(0.0)
    val percentageChange: StateFlow<Double> = _percentageChange.asStateFlow()

    init {
        // Load user data (fiat balance, etc.) once from userSession
        viewModelScope.launch {
            userSession.userData.collect { user ->
                if (user != null) {
                    _userBalance.value = user.balance
                    // After we set userBalance, also recalc total
                    recalcTotalValue()
                }
            }
        }
    }

    /**
     * Load user assets from Firestore, build local map {assetId -> quantity}
     */
    fun loadUserAssets() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            try {
                val snapshot = db.collection("users")
                    .document(uid)
                    .collection("trades")
                    .get()
                    .await()

                val trades = snapshot.documents.mapNotNull { it.toObject(TradeRecord::class.java) }
                val assetMap = mutableMapOf<String, Double>()

                // Build asset map summing BUY as +quantity, SELL as -quantity
                for (trade in trades) {
                    val delta = if (trade.type == "buy") trade.quantity else -trade.quantity
                    val oldQty = assetMap[trade.assetId] ?: 0.0
                    assetMap[trade.assetId] = oldQty + delta
                }
                // Filter out zero or negative
                val cleaned = assetMap.filterValues { it > 0.0 }
                _userAssets.value = cleaned

                // Immediately recalc portfolio after we loaded
                recalcPortfolioValue()
            } catch (e: Exception) {
                Log.e("TRADE_ERROR", "Failed to load user assets: ${e.message}")
            }
        }
    }

    /**
     * Observe price updates from the CryptoViewModel.
     * Each time we get new prices, recalculate portfolio value + total.
     */
    fun observePriceUpdates(priceFlow: StateFlow<Map<String, Double>>) {
        viewModelScope.launch {
            priceFlow.collect { updates ->
                priceUpdates = updates
                recalcPortfolioValue()
            }
        }
    }

    /**
     * Recalculate portfolioValue using current userAssets and the latest priceUpdates.
     */
    private fun recalcPortfolioValue() {
        val assets = _userAssets.value
        val totalAssetsValue = assets.entries.sumOf { (assetId, qty) ->
            val currentPrice = priceUpdates[assetId] ?: 0.0
            qty * currentPrice
        }
        _portfolioValue.value = totalAssetsValue
        recalcTotalValue()
    }

    /**
     * Recalculate totalValue = userBalance + portfolioValue
     */
    private fun recalcTotalValue() {
        _totalValue.value = _userBalance.value + _portfolioValue.value
        calculatePercentageChange()
    }

    /**
     * Execute a trade: BUY or SELL some quantity of an asset at a given price
     */
    fun executeTrade(
        type: TradeType,
        assetId: String,
        assetName: String,
        currentPrice: Double,
        quantity: Double
    ) {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid
            val user = userSession.userData.value

            if (uid == null || user == null) {
                _tradeError.value = "User not authenticated"
                return@launch
            }

            // The cost (or proceeds) of the trade in fiat
            val tradeCost = currentPrice * quantity

            // Validate user's current fiat balance or owned assets
            val updatedUser = when (type) {
                TradeType.BUY -> {
                    if (_userBalance.value < tradeCost) {
                        _tradeError.value = "Not enough balance to complete purchase"
                        return@launch
                    }
                    val newFiatBalance = _userBalance.value - tradeCost
                    _userBalance.value = newFiatBalance
                    // Also reflect in userSession userData
                    user.copy(
                        balance = newFiatBalance,
                        tradeVolume = user.tradeVolume + tradeCost.toInt()
                    )
                }
                TradeType.SELL -> {
                    val ownedAmount = getUserAssetAmount(assetId)
                    if (ownedAmount < quantity) {
                        _tradeError.value = "Not enough of $assetName to sell"
                        return@launch
                    }
                    val newFiatBalance = _userBalance.value + tradeCost
                    _userBalance.value = newFiatBalance
                    // Also reflect in userSession userData
                    user.copy(
                        balance = newFiatBalance,
                        tradeVolume = user.tradeVolume + tradeCost.toInt()
                    )
                }
            }

            // Update userSession with new user data
            userSession.updateUser(updatedUser)

            // Update local userAssets
            updateLocalAssets(type, assetId, quantity)

            // Recalc after local changes
            recalcPortfolioValue()

            // Save the trade in Firestore
            val tradeRecord = TradeRecord(
                type = type.name.lowercase(),
                assetId = assetId,
                assetName = assetName,
                price = currentPrice,
                quantity = quantity,
                totalValue = tradeCost,
                timestamp = System.currentTimeMillis()
            )
            try {
                db.collection("users")
                    .document(uid)
                    .collection("trades")
                    .add(tradeRecord)
            } catch (e: Exception) {
                Log.e("TRADE_ERROR", "Failed to save trade history: ${e.message}")
            }

            try {
                db.collection("users")
                    .document(uid)
                    .set(updatedUser)
            } catch (e: Exception) {
                Log.e("TRADE_ERROR", "Failed to update user data: ${e.message}")
            }

            // Reset error
            _tradeError.value = null
        }
    }

    /**
     * Retrieve how many of a particular asset user owns, based on all trades in Firestore
     * (Used just for SELL validation)
     */
    private suspend fun getUserAssetAmount(assetId: String): Double {
        val uid = auth.currentUser?.uid ?: return 0.0
        val snapshot = db.collection("users")
            .document(uid)
            .collection("trades")
            .get()
            .await()

        val trades = snapshot.documents.mapNotNull { it.toObject(TradeRecord::class.java) }
        return trades.filter { it.assetId == assetId }.sumOf { trade ->
            if (trade.type == "buy") trade.quantity else -trade.quantity
        }
    }

    /**
     * Update local userAssets map to reflect new quantity after a trade
     */
    private fun updateLocalAssets(type: TradeType, assetId: String, quantity: Double) {
        val currentMap = _userAssets.value.toMutableMap()
        val delta = if (type == TradeType.BUY) quantity else -quantity
        val oldQty = currentMap[assetId] ?: 0.0
        val newQty = (oldQty + delta).coerceAtLeast(0.0)
        if (newQty <= 0.0) {
            currentMap.remove(assetId)
        } else {
            currentMap[assetId] = newQty
        }
        _userAssets.value = currentMap
    }

    fun calculatePercentageChange() {
        val initial = userSession.userData.value?.initialTotalBalance ?: return
        val current = _totalValue.value
        if (initial != 0.0) {
            val percent = ((current - initial) / initial) * 100
            _percentageChange.value = percent
        }
    }
}
