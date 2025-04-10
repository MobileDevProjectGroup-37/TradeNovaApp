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
import com.example.traderapp.ui.screens.components.PortfolioItem
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.TradeViewModel

@SuppressLint("DefaultLocale")
@Composable
fun BuyTab(
    navController: NavController,
    cryptoViewModel: CryptoViewModel,
    tradeViewModel: TradeViewModel,
) {


    val cryptoList by cryptoViewModel.cryptoList.collectAsState()
    val priceUpdates by cryptoViewModel.priceUpdates.collectAsState()

    var selectedCrypto by remember { mutableStateOf<CryptoDto?>(null) }
    var fiatInput by remember { mutableStateOf("") }
    var cryptoInput by remember { mutableStateOf("") }

    val tradeError by tradeViewModel.tradeError.collectAsState()

    val conversionRate = selectedCrypto?.let {
        priceUpdates[it.id] ?: it.priceUsd.toDoubleOrNull()
    } ?: 1.0

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

        Text("Choose crypto to buy", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedCrypto == null) {
            LazyColumn {
                items(cryptoList) { crypto ->
                    val price = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0

                    PortfolioItem(
                        crypto = crypto.name,
                        currentPrice = String.format("%.2f", price),
                        onClick = { selectedCrypto = crypto }
                    )
                }
            }
        } else {

            OutlinedButton(
                onClick = { selectedCrypto = null },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.small.copy(all = CornerSize(10.dp))
            ) {
                Text(selectedCrypto?.name ?: "Chosen crypto",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp) )
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

            Text("You will get this amount of BTC:")
            OutlinedTextField(
                value = cryptoInput,
                onValueChange = { cryptoInput = it },
                label = { Text("BTC Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = "Buy",
                onClick = {
                    val price = conversionRate
                    val quantity = cryptoInput.toDoubleOrNull()?: 0.0
                    val id = selectedCrypto?.id.orEmpty()
                    val name = selectedCrypto?.name.orEmpty()
                    Log.d("BUY_TAB", "Buy clicked. id=$id, name=$name, price=$price, quantity=$quantity")
                    if (id.isNotEmpty() && name.isNotEmpty() && price > 0 && quantity > 0) {
                        Log.d("BUY_TAB", "Valid input, calling executeTrade()")
                        tradeViewModel.executeTrade(
                            type = TradeType.BUY,
                            assetId = id,
                            assetName = name,
                            currentPrice = price,
                            quantity = quantity
                        )
                    }
                    else{
                        Log.e("BUY_TAB", "Invalid input. Skipping trade.")
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = Color.White
            )
        }
    }
}
