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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val userSession: UserSession
) : ViewModel() {

    private val _tradeError = MutableStateFlow<String?>(null)
    val tradeError: StateFlow<String?> = _tradeError

    val _userAssets = MutableStateFlow<Map<String, Double>>(emptyMap())
    val userAssets: StateFlow<Map<String, Double>> = _userAssets

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



            Log.d("TRADE", "Starting trade execution...")
            Log.d("TRADE", "uid = $uid")
            Log.d("TRADE", "user = $user")

            if (uid == null || user == null) {
                _tradeError.value = "User not authenticated"
                return@launch
            }

            val totalValue = currentPrice * quantity


            Log.d("TRADE", "Trade params: type=$type, asset=$assetName, price=$currentPrice, quantity=$quantity, totalValue=$totalValue")
            Log.d("TRADE", "User balance: ${user.balance}")

            val updatedUser = when (type) {
                TradeType.BUY -> {
                    if (user.balance < totalValue) {
                        _tradeError.value = "Not enough balance to complete purchase"
                        return@launch
                    }

                    user.copy(
                        balance = user.balance - totalValue,
                        tradeVolume = user.tradeVolume + totalValue.toInt()
                    )
                }

                TradeType.SELL -> {
                    // TODO: check that there is sufficient amount of an asset
                    user.copy(
                        balance = user.balance + totalValue,
                        tradeVolume = user.tradeVolume + totalValue.toInt()
                    )
                }
            }

            //
            userSession.updateUser(updatedUser)

            // save to history
            val tradeRecord = TradeRecord(
                type = type.name.lowercase(),
                assetId = assetId,
                assetName = assetName,
                price = currentPrice,
                quantity = quantity,
                totalValue = totalValue,
                timestamp = System.currentTimeMillis()
            )

            try {
                db.collection("users").document(uid).collection("trades").add(tradeRecord)
                Log.d("TRADE", "Trade history saved.")
            } catch (e: Exception) {
                Log.e("TRADE_ERROR", "Failed to save trade history: ${e.message}")
            }

            try {
                db.collection("users").document(uid).set(updatedUser)
                Log.d("TRADE", "User data updated in Firestore.")
            } catch (e: Exception) {
                Log.e("TRADE_ERROR", "Failed to update user data: ${e.message}")
            }

            _tradeError.value = null
            Log.d("TRADE", "Trade completed successfully.")
        }
    }
    fun loadUserAssets() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("trades")
            .get()
            .addOnSuccessListener { snapshot ->
                val trades = snapshot.documents.mapNotNull { it.toObject(TradeRecord::class.java) }

                val assetMap = mutableMapOf<String, Double>()

                for (trade in trades) {
                    val delta = if (trade.type == "buy") trade.quantity else -trade.quantity
                    assetMap[trade.assetId] = (assetMap[trade.assetId] ?: 0.0) + delta
                }


                _userAssets.value = assetMap.filterValues { it > 0 }
            }
    }
}

