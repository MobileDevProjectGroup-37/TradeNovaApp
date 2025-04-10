package com.example.traderapp.ui.screens.trade

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
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
import com.example.traderapp.ui.screens.components.PortfolioItem
import com.example.traderapp.ui.screens.components.bars.SearchBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.TradeViewModel
import kotlin.random.Random

@SuppressLint("DefaultLocale")
@Composable
fun BuyTab(
    navController: NavController,
    cryptoViewModel: CryptoViewModel,
    tradeViewModel: TradeViewModel,
    userSession: UserSession
) {
    val cryptoList by cryptoViewModel.cryptoList.collectAsState()
    val priceUpdates by cryptoViewModel.priceUpdates.collectAsState()
    val tradeError by tradeViewModel.tradeError.collectAsState()
    val userData by userSession.userData.collectAsState()

    val balance = userData?.balance ?: 0.0

    var selectedCrypto by remember { mutableStateOf<CryptoDto?>(null) }
    var fiatInput by remember { mutableStateOf("") }
    var cryptoInput by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val conversionRate = selectedCrypto?.let {
        priceUpdates[it.id] ?: it.priceUsd.toDoubleOrNull()
    } ?: 1.0

    // 🔁 Автоматический перерасчёт
    LaunchedEffect(fiatInput, selectedCrypto) {
        selectedCrypto?.let {
            fiatInput.toDoubleOrNull()?.let { amount ->
                if (conversionRate != 0.0) {
                    cryptoInput = String.format("%.6f", amount / conversionRate)
                }
            }
        }
    }

    LaunchedEffect(cryptoInput, selectedCrypto) {
        selectedCrypto?.let {
            cryptoInput.toDoubleOrNull()?.let { amount ->
                fiatInput = String.format("%.2f", amount * conversionRate)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BalanceHeader(balance)
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedCrypto == null) {

            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            val filteredList = if (searchQuery.isBlank()) {
                cryptoList.shuffled().take(3) // 🔀 Показываем 3 случайные
            } else {
                cryptoList.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredList) { crypto ->
                    val price = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
                    PortfolioItem(
                        crypto = crypto.name,
                        currentPrice = String.format("%.2f", price),
                        onClick = { selectedCrypto = crypto },
                        selected = selectedCrypto?.id == crypto.id,
                        showHint = true
                    )
                }
            }

        } else {
            // ✅ После выбора крипты
            OutlinedButton(
                onClick = { selectedCrypto = null },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.small.copy(all = CornerSize(10.dp))
            ) {
                Text(
                    selectedCrypto?.name ?: "Chosen crypto",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("Enter the sum in USD:")
            OutlinedTextField(
                value = fiatInput,
                onValueChange = { fiatInput = it },
                label = { Text("Sum in USD") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("You will get this amount of ${selectedCrypto?.symbol ?: "crypto"}:")
            OutlinedTextField(
                value = cryptoInput,
                onValueChange = { cryptoInput = it },
                label = { Text("Crypto amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                enabled = false
            )

            Spacer(modifier = Modifier.height(18.dp))

            CustomButton(
                text = "Buy",
                onClick = {
                    val price = conversionRate
                    val quantity = cryptoInput.toDoubleOrNull() ?: 0.0
                    val id = selectedCrypto?.id.orEmpty()
                    val name = selectedCrypto?.name.orEmpty()

                    Log.d("BUY_TAB", "Buy clicked. id=$id, name=$name, price=$price, quantity=$quantity")

                    if (id.isNotEmpty() && name.isNotEmpty() && price > 0 && quantity > 0) {
                        tradeViewModel.executeTrade(
                            type = TradeType.BUY,
                            assetId = id,
                            assetName = name,
                            currentPrice = price,
                            quantity = quantity
                        )
                    } else {
                        Log.e("BUY_TAB", "Invalid input. Skipping trade.")
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = Color.White
            )
        }
    }
}
