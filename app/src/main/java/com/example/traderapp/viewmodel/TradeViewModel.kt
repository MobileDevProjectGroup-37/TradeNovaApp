package com.example.traderapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.model.TradeRecord
import com.example.traderapp.data.model.TradeType
import com.example.traderapp.data.network.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlinx.coroutines.flow.first

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val userSession: UserSession,
) : ViewModel() {

    // region === Loading State ===
    // This flag controls whether data is still loading (to avoid partial updates in UI).
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    // endregion
    // region === State ===

    private val _tradeError = MutableStateFlow<String?>(null)
    val tradeError: StateFlow<String?> = _tradeError.asStateFlow()

    private val _userBalance = MutableStateFlow(0.0)
    val userBalance: StateFlow<Double> = _userBalance.asStateFlow()

    private val _userAssets = MutableStateFlow<Map<String, Double>>(emptyMap())
    val userAssets: StateFlow<Map<String, Double>> = _userAssets.asStateFlow()

    private val _portfolioValue = MutableStateFlow(0.0)
    val portfolioValue: StateFlow<Double> = _portfolioValue.asStateFlow()

    private val _totalValue = MutableStateFlow(0.0)
    val totalValue: StateFlow<Double> = _totalValue.asStateFlow()

    private val _percentageChange = MutableStateFlow(0.0)
    val percentageChange: StateFlow<Double> = _percentageChange.asStateFlow()

    private var priceUpdates: Map<String, Double> = emptyMap()

    // endregion


    // region === Loading All Data Once ===
    // This function ensures we load user data, user assets, prices, etc. all at once.
    fun loadInitialData(cryptoViewModel: CryptoViewModel) {
        viewModelScope.launch {
            _isLoading.value = true
            try {

                userSession.loadUserData()
                val user = userSession.userData.value
                if (user != null) {
                    _userBalance.value = user.balance
                }

                loadUserAssets()

                // 2. Load cryptolist and wait untill it is ready
                cryptoViewModel.cryptoList
                    .filter { it.isNotEmpty() }
                    .first()

                // 3. Тlaunch preload only after we get full pryptolist
                preloadCryptoList(cryptoViewModel.cryptoList.value)

            // 4. Wait till get some prices
                val prices = cryptoViewModel.priceUpdates
                    .filter { it.isNotEmpty() }
                    .first()

                priceUpdates = prices
                recalcPortfolioValue()

            // 5. Turn off the loader
                _isLoading.value = false

            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("TRADE_ERROR", "Failed to load initial data: ${e.message}")
            }
        }
    }
    // endregion

    // region === Public API ===

    /**
     * Make this function 'suspend' so we can call it sequentially and actually
     * wait for the Firestore response before returning. No additional 'viewModelScope.launch' inside!
     */
    suspend fun loadUserAssets() {
        val uid = auth.currentUser?.uid ?: return
        try {
            // Firestore query in the same coroutine
            val snapshot = db.collection("users")
                .document(uid)
                .collection("trades")
                .get()
                .await()

            val trades = snapshot.documents.mapNotNull { it.toObject(TradeRecord::class.java) }
            val assetMap = mutableMapOf<String, Double>()

            for (trade in trades) {
                val delta = if (trade.type == "buy") trade.quantity else -trade.quantity
                val oldQty = assetMap[trade.assetId] ?: 0.0
                assetMap[trade.assetId] = oldQty + delta
            }

            val cleaned = assetMap.filterValues { it > 0.0 }
            _userAssets.value = cleaned

            recalcPortfolioValue()

        } catch (e: Exception) {
            Log.e("TRADE_ERROR", "Failed to load user assets: ${e.message}")
        }
    }

    // Safe trafic version
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

            val tradeCost = currentPrice * quantity
            val currentBalance = _userBalance.value

            val ownedAmount = _userAssets.value[assetId] ?: 0.0
            val newBalance = when (type) {
                TradeType.BUY -> {
                    if (currentBalance < tradeCost) {
                        _tradeError.value = "Not enough balance to complete purchase"
                        return@launch
                    }
                    currentBalance - tradeCost
                }
                TradeType.SELL -> {
                    if (ownedAmount < quantity) {
                        _tradeError.value = "Not enough of $assetName to sell"
                        return@launch
                    }
                    currentBalance + tradeCost
                }
            }

            val updatedUser = user.copy(
                balance = newBalance,
                tradeVolume = user.tradeVolume + tradeCost.toInt()
            )

            _userBalance.value = newBalance
            userSession.updateUser(updatedUser)
            updateLocalAssets(type, assetId, quantity)
            recalcPortfolioValue()

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
                val batch = db.batch()
                val userRef = db.collection("users").document(uid)
                val tradeRef = userRef.collection("trades").document()

                batch.set(userRef, updatedUser)
                batch.set(tradeRef, tradeRecord)

                batch.commit().await()
            } catch (e: Exception) {
                Log.e("TRADE_ERROR", "Failed to batch write: ${e.message}")
            }

            _tradeError.value = null
        }
    }
    // endregion

    // region === Helpers ===
    //get pointer to cryptolist from cryptoviewmodel
    private var cryptoList: List<CryptoDto> = emptyList()

    fun preloadCryptoList(list: List<CryptoDto>) {
        cryptoList = list //http resp
        Log.d("DEBUG", "cryptoList ids = ${cryptoList.map { it.id }}")
    }
    //updateprices = local structure to keep
    private fun recalcPortfolioValue() {
        // Get the user's current crypto holdings
        val assets = _userAssets.value

        // Sum the total value of all crypto assets
        val totalAssetsValue = assets.entries.sumOf { (id, qty) ->
            // Try to find a fallback price from the original crypto list if live price is unavailable
            val fallbackPrice = cryptoList.find { it.id == id }?.priceUsd?.toDoubleOrNull() ?: 0.0
            Log.d("FALLBACK_CHECK 1", "For $id => fallbackPrice=$fallbackPrice")

            // Use live price if available, otherwise use the fallback static price
            val currentPrice = priceUpdates[id] ?: fallbackPrice

            // Multiply quantity by price to get total value of this asset
            qty * currentPrice
        }

        // Update the portfolio value with the calculated total
        _portfolioValue.value = totalAssetsValue

        // Recalculate the total value (portfolio + fiat balance)
        recalcTotalValue()
    }
    //ratings
    private fun updateUserRoiInDatabase(newRoi: Double) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)

        userRef.update("profit", newRoi)
            .addOnSuccessListener {
                Log.d("ROI_UPDATE", "ROI updated successfully in Firestore: $newRoi")
            }
            .addOnFailureListener { e ->
                Log.e("ROI_UPDATE", "Failed to update ROI: ${e.message}")
            }
    }

    private fun recalcTotalValue() {
        _totalValue.value = _userBalance.value + _portfolioValue.value
        calculatePercentageChange()
    }

    private fun calculatePercentageChange() {
        val initial = userSession.userData.value?.initialTotalBalance ?: return
        val current = _totalValue.value
        if (initial != 0.0) {
            val newRoi = ((current - initial) / initial) * 100
            _percentageChange.value = newRoi


            updateUserRoiInDatabase(newRoi)
        }
    }


    private fun updateLocalAssets(type: TradeType, assetId: String, quantity: Double) {
        Log.d("LOCAL_ASSETS", "Updating local assets: type=$type, assetId=$assetId, qty=$quantity")

        val currentMap = _userAssets.value.toMutableMap()
        val delta = if (type == TradeType.BUY) quantity else -quantity
        val oldQty = currentMap[assetId] ?: 0.0
        val newQty = (oldQty + delta).coerceAtLeast(0.0)

        Log.d("LOCAL_ASSETS", "Old qty=$oldQty => new qty=$newQty")

        if (newQty <= 0.0) {
            currentMap.remove(assetId)
            Log.d("LOCAL_ASSETS", "Removed assetId=$assetId, because newQty=$newQty <= 0")
        } else {
            currentMap[assetId] = newQty
        }
        _userAssets.value = currentMap

        Log.d("LOCAL_ASSETS", "Updated userAssets=${_userAssets.value}")
    }


    private suspend fun getUserAssetAmount(assetId: String): Double {
        val uid = auth.currentUser?.uid ?: return 0.0
        val snapshot = db.collection("users")
            .document(uid)
            .collection("trades")
            .get()
            .await()

        val trades = snapshot.documents.mapNotNull { it.toObject(TradeRecord::class.java) }
        return trades.filter { it.assetId == assetId }
            .sumOf { if (it.type == "buy") it.quantity else -it.quantity }
    }

    fun getAssetUsdValue(assetId: String): Double {

        val amount = userAssets.value[assetId] ?: 0.0

        val livePrice = priceUpdates[assetId]
        val fallbackPrice = cryptoList.find { it.id == assetId }?.priceUsd?.toDoubleOrNull()

        val price = livePrice ?: fallbackPrice ?: 0.0
        Log.d("DEBUG", "asset=$assetId, amount=$amount, livePrice=$livePrice, fallbackPrice=$fallbackPrice, finalPrice=$price")
        return amount * price
    }

//

    // endregion

    //exchange region
    fun executeExchange(
        fromAssetId: String,
        toAssetId: String,
        fromAmount: Double,
        priceMap: Map<String, Double>
    ) {
        viewModelScope.launch {
            Log.d("EXCHANGE", "Starting exchange from=$fromAssetId to=$toAssetId amount=$fromAmount")

            val uid = auth.currentUser?.uid
            val user = userSession.userData.value

            if (uid == null || user == null) {
                _tradeError.value = "User not authenticated"
                Log.e("EXCHANGE", "User not authenticated!")
                return@launch
            }

            val fromBalance = _userAssets.value[fromAssetId] ?: 0.0
            Log.d("EXCHANGE", "User has fromBalance=$fromBalance of $fromAssetId")

            if (fromBalance < fromAmount) {
                _tradeError.value = "Not enough of $fromAssetId to exchange"
                Log.e("EXCHANGE", "Not enough $fromAssetId: need $fromAmount, have $fromBalance")
                return@launch
            }

            val fromPrice = priceMap[fromAssetId] ?: 0.0
            val toPrice = priceMap[toAssetId] ?: 0.0
            Log.d("EXCHANGE", "Prices: fromPrice=$fromPrice, toPrice=$toPrice")

            if (fromPrice <= 0 || toPrice <= 0) {
                _tradeError.value = "Invalid price data"
                Log.e("EXCHANGE", "Invalid price data: fromPrice=$fromPrice, toPrice=$toPrice")
                return@launch
            }

            val fromValueUsd = fromAmount * fromPrice
            val toAmount = fromValueUsd / toPrice
            Log.d("EXCHANGE", "Exchanging => fromValueUsd=$fromValueUsd => toAmount=$toAmount")

            // 1) Update local assets
            updateLocalAssets(TradeType.SELL, fromAssetId, fromAmount)
            updateLocalAssets(TradeType.BUY, toAssetId, toAmount)

            // 2) Recalc portfolio
            recalcPortfolioValue()

            // 3) Save both trades in Firestore
            val timestamp = System.currentTimeMillis()
            val records = listOf(
                TradeRecord(
                    type = "sell",
                    assetId = fromAssetId,
                    assetName = fromAssetId,
                    price = fromPrice,
                    quantity = fromAmount,
                    totalValue = fromValueUsd,
                    timestamp = timestamp
                ),
                TradeRecord(
                    type = "buy",
                    assetId = toAssetId,
                    assetName = toAssetId,
                    price = toPrice,
                    quantity = toAmount,
                    totalValue = fromValueUsd,
                    timestamp = timestamp
                )
            )

            try {
                val docRef = db.collection("users").document(uid)
                val tradesRef = docRef.collection("trades")
                records.forEach { rec ->
                    Log.d("EXCHANGE", "Saving trade=$rec")
                    tradesRef.add(rec)
                }
            } catch (e: Exception) {
                Log.e("EXCHANGE_ERROR", "Failed to save exchange: ${e.message}")
            }

            _tradeError.value = null
            Log.d("EXCHANGE", "Exchange finished successfully")
        }
    }

}