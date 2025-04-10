package com.example.traderapp.ui.screens.trade

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.model.TradeType
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.TradeViewModel


@SuppressLint("DefaultLocale")
@Composable
fun SellTab(
    navController: NavController,
    cryptoViewModel: CryptoViewModel,
    tradeViewModel: TradeViewModel,
    userSession: UserSession
) {
    val cryptoList by cryptoViewModel.cryptoList.collectAsState()
    val userAssets by tradeViewModel.userAssets.collectAsState()

    // загружаем активы при первом входе
    LaunchedEffect(Unit) {
        tradeViewModel.loadUserAssets()
    }

    var selectedCrypto by remember { mutableStateOf<CryptoDto?>(null) }
    var cryptoInput by remember { mutableStateOf("") }

    LaunchedEffect(userAssets) {
        if (selectedCrypto != null) {
            val remaining = userAssets[selectedCrypto!!.id] ?: 0.0
            if (remaining <= 0.0) {
                selectedCrypto = null
                cryptoInput = ""
            }
        }
    }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Choose crypto to sell", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { selectedCrypto = null },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedCrypto?.name ?: "Choose crypto to sell")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filter assets which are larger than 0
        val availableCryptos = cryptoList.filter { crypto ->
            userAssets[crypto.id]?.let { it > 0.0 } == true
        }

        if (availableCryptos.isNotEmpty()) {
            LazyColumn {
                items(availableCryptos) { crypto ->
                    val amount = userAssets[crypto.id] ?: 0.0
                    val valueUsd = tradeViewModel.getAssetUsdValue(crypto.id)

                    Text(
                        text = "${crypto.name} — amount: ${"%.4f".format(amount)} — \$${"%.2f".format(valueUsd)}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { selectedCrypto = crypto }
                    )
                }
            }
        } else {
            Text("You don't have any crypto to sell.")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedCrypto != null) {
            val amount = userAssets[selectedCrypto!!.id] ?: 0.0
            val valueUsd = tradeViewModel.getAssetUsdValue(selectedCrypto!!.id)

            Text("You own ${"%.4f".format(amount)} worth \$${"%.2f".format(valueUsd)}")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Enter the amount of ${selectedCrypto?.name} to sell:")
            OutlinedTextField(
                value = cryptoInput,
                onValueChange = { cryptoInput = it },
                label = { Text("Amount to sell") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = "Sell",
                onClick = {
                    val quantity = cryptoInput.toDoubleOrNull() ?: 0.0
                    val price = tradeViewModel.getAssetUsdValue(selectedCrypto!!.id) / amount
                    val assetId = selectedCrypto?.id.orEmpty()
                    val assetName = selectedCrypto?.name.orEmpty()

                    if (quantity > 0 && price > 0 && assetId.isNotEmpty()) {
                        tradeViewModel.executeTrade(
                            type = TradeType.SELL,
                            assetId = assetId,
                            assetName = assetName,
                            currentPrice = price,
                            quantity = quantity
                        )
                        cryptoInput = ""
                        selectedCrypto = null
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = Color.White
            )
        }
    }
}

