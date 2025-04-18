package com.example.traderapp.ui.screens.trade

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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.model.TradeType
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.ui.screens.portfolio.PortfolioItem
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.TradeViewModel

@Composable
fun SellTab(
    navController: NavController,
    cryptoViewModel: CryptoViewModel,
    tradeViewModel: TradeViewModel,
    userSession: UserSession
) {
    val cryptoList by cryptoViewModel.cryptoList.collectAsState()
    val userAssets by tradeViewModel.userAssets.collectAsState()
    val priceUpdates by cryptoViewModel.priceUpdates.collectAsState()

    var selectedCrypto by remember { mutableStateOf<CryptoDto?>(null) }
    var cryptoInput by remember { mutableStateOf("") }
    var showConfirm by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tradeViewModel.loadUserAssets()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedCrypto == null) {
            val availableCryptos = cryptoList.filter { crypto ->
                (userAssets[crypto.id] ?: 0.0) > 0.0
            }

            if (availableCryptos.isNotEmpty()) {
                LazyColumn {
                    items(availableCryptos) { crypto ->
                        val amount = userAssets[crypto.id] ?: 0.0
                        val price = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
                        val valueUsd = amount * price

                        PortfolioItem(
                            crypto = crypto.name,
                            currentPrice = "${String.format("%.2f", price)}",
                            amount = String.format("%.4f", amount),
                            usdValue = String.format("%.2f", valueUsd),
                            onClick = { selectedCrypto = crypto },
                            selected = selectedCrypto?.id == crypto.id,
                            showHint = true,
                            hintText = "Tap to sell"
                        )
                    }
                }
            } else {
                Text("You don't have any crypto to sell.")
            }
        } else {
            val amount = userAssets[selectedCrypto!!.id] ?: 0.0
            val pricePerUnit = priceUpdates[selectedCrypto!!.id] ?: selectedCrypto!!.priceUsd.toDoubleOrNull() ?: 0.0
            val quantity = cryptoInput.toDoubleOrNull() ?: 0.0
            val balance = userSession.userData.value?.balance ?: 0.0

            BalanceHeader(balance)
            Spacer(modifier = Modifier.height(16.dp))


            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Selling ${selectedCrypto!!.name}", style = MaterialTheme.typography.titleLarge)
                        TextButton(onClick = {
                            selectedCrypto = null
                            cryptoInput = ""
                        }) {
                            Text("Change")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("You own ${"%.4f".format(amount)} worth \$${"%.2f".format(amount * pricePerUnit)}")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Enter amount to sell:")
                    OutlinedTextField(
                        value = cryptoInput,
                        onValueChange = { cryptoInput = it },
                        label = { Text("Crypto amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomButton(
                        text = "Sell",
                        onClick = {
                            if (quantity > 0 && quantity <= amount) {
                                showConfirm = true
                            }
                        },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor = Color.White,
                        showBorder = false,
                        fontSize = 20.sp
                    )
                }
            }

            if (showConfirm) {
                AlertDialog(
                    onDismissRequest = { showConfirm = false },
                    confirmButton = {
                        TextButton(onClick = {
                            tradeViewModel.executeTrade(
                                type = TradeType.SELL,
                                assetId = selectedCrypto!!.id,
                                assetName = selectedCrypto!!.name,
                                currentPrice = pricePerUnit,
                                quantity = quantity
                            )
                            showConfirm = false
                            showSuccess = true
                            selectedCrypto = null
                            cryptoInput = ""
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirm = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Confirm sale") },
                    text = {
                        Text("Sell $cryptoInput ${selectedCrypto?.symbol} for \$${"%.2f".format(quantity * pricePerUnit)}?")
                    }
                )
            }

            if (showSuccess) {
                AlertDialog(
                    onDismissRequest = { showSuccess = false },
                    confirmButton = {
                        TextButton(onClick = { showSuccess = false }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Success!") },
                    text = {
                        Text("Sale completed successfully.")
                    }
                )
            }
        }
    }
}
