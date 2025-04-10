package com.example.traderapp.ui.screens.trade

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.model.TradeType
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.ui.screens.components.PortfolioItem
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.TradeViewModel

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

    var selectedCrypto by remember { mutableStateOf<CryptoDto?>(null) }
    var fiatInput by remember { mutableStateOf("") }
    var cryptoInput by remember { mutableStateOf("") }

    val tradeError by tradeViewModel.tradeError.collectAsState()
    val userData by userSession.userData.collectAsState()
    val balance = userData?.balance ?: 0.0

    val conversionRate = selectedCrypto?.let {
        priceUpdates[it.id] ?: it.priceUsd.toDoubleOrNull()
    } ?: 1.0

    // 💡 Добавляем постраничный просмотр
    var currentIndex by remember { mutableStateOf(0) }
    val itemsPerPage = 3
    val itemsToShow = cryptoList.drop(currentIndex).take(itemsPerPage)

    // 💰 Автоматическая конвертация валют
    LaunchedEffect(fiatInput, selectedCrypto) {
        if (selectedCrypto != null) {
            val amountDouble = fiatInput.toDoubleOrNull()
            if (amountDouble != null && conversionRate != 0.0) {
                cryptoInput = String.format("%.6f", amountDouble / conversionRate)
            }
        }
    }

    LaunchedEffect(cryptoInput, selectedCrypto) {
        if (selectedCrypto != null) {
            val cryptoDouble = cryptoInput.toDoubleOrNull()
            if (cryptoDouble != null) {
                fiatInput = String.format("%.2f", cryptoDouble * conversionRate)
            }
        }
    }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        // 💰 Баланс
        BalanceHeader(balance)

        // 🔄 Если крипта не выбрана — показываем список с пагинацией
        if (selectedCrypto == null) {
            Column {
                itemsToShow.forEach { crypto ->
                    val price = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
                    PortfolioItem(
                        crypto = crypto.name,
                        currentPrice = String.format("%.2f", price),
                        onClick = { selectedCrypto = crypto },
                        selected = selectedCrypto?.id == crypto.id,
                        showHint = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { if (currentIndex > 0) currentIndex -= itemsPerPage },
                        enabled = currentIndex > 0
                    ) {
                        Text("Back")
                    }

                    Button(
                        onClick = {
                            if (currentIndex + itemsPerPage < cryptoList.size) {
                                currentIndex += itemsPerPage
                            }
                        },
                        enabled = currentIndex + itemsPerPage < cryptoList.size
                    ) {
                        Text("Next")
                    }
                }
            }
        } else {
            // ✅ После выбора — отображаем форму покупки
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

            Spacer(modifier = Modifier.height(16.dp))

            Text("Enter the sum in USD:")
            OutlinedTextField(
                value = fiatInput,
                onValueChange = { fiatInput = it },
                label = { Text("Sum in USD") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("You will get this amount of ${selectedCrypto?.symbol ?: "crypto"}:")
            OutlinedTextField(
                value = cryptoInput,
                onValueChange = { cryptoInput = it },
                label = { Text("Crypto amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

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
