package com.example.traderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traderapp.data.UserData
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

    fun executeTrade(
        type: TradeType,
        assetId: String,
        assetName: String,
        currentPrice: Double,
        quantity: Int
    ) {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid
            val user = userSession.userData.value

            if (uid == null || user == null) {
                _tradeError.value = "User not authenticated"
                return@launch
            }

            val totalValue = currentPrice * quantity

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
            val tradeRecord = mapOf(
                "type" to type.name.lowercase(),
                "assetId" to assetId,
                "assetName" to assetName,
                "price" to currentPrice,
                "quantity" to quantity,
                "totalValue" to totalValue,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("users").document(uid).collection("trades").add(tradeRecord)

            // Update data in users collection
            db.collection("users").document(uid).set(updatedUser)

            _tradeError.value = null
        }
    }

}
