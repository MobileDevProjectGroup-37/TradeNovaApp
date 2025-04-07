package com.example.traderapp.ui.screens.trade

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
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.CryptoViewModel


@Composable
fun SellTab(navController: NavController) {
    // get ViewModel via hiltViewModel()
    val viewModel: CryptoViewModel = hiltViewModel()

    val cryptoList by viewModel.cryptoList.collectAsState()
    val priceUpdates by viewModel.priceUpdates.collectAsState()
    val userBalance by viewModel.userBalance.collectAsState()

    var selectedCrypto by remember { mutableStateOf<CryptoDto?>(null) }
    var cryptoInput by remember { mutableStateOf("") }

    val conversionRate = selectedCrypto?.let {
        priceUpdates[it.id] ?: it.priceUsd.toDoubleOrNull()
    } ?: 1.0

    LaunchedEffect(cryptoInput, selectedCrypto) {
        if (selectedCrypto != null) {
            val cryptoDouble = cryptoInput.toDoubleOrNull()
            if (cryptoDouble != null) {
                // Обновление суммы в USD на основе введенного количества криптовалюты
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

        // Filter crypto list to show only those that the user has
        val availableCryptos = cryptoList.filter { crypto ->
            userBalance[crypto.id]?.let { it > 0 } == true  // Filtered by user's balance
        }

        //If a user has a list of crypto which he can sell, show it
        if (availableCryptos.isNotEmpty()) {
            LazyColumn {
                items(availableCryptos) { crypto ->
                    Text(
                        text = crypto.name,
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

        // Enter the amount of crypto to sell
        if (selectedCrypto != null) {
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
                    // logic for selling crypto
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = Color.White
            )
        }
    }
}
