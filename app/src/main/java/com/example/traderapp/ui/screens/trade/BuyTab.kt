package com.example.traderapp.ui.screens.trade


import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.data.model.CryptoDto
import com.example.traderapp.data.model.TradeType
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.ui.screens.portfolio.PortfolioItem
import com.example.traderapp.ui.screens.components.bars.SearchBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.CryptoViewModel
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
    val tradeError by tradeViewModel.tradeError.collectAsState()
    val userData by userSession.userData.collectAsState()
    val balance = userData?.balance ?: 0.0

    var selectedCrypto by remember { mutableStateOf<CryptoDto?>(null) }
    var confirmedCrypto by remember { mutableStateOf<CryptoDto?>(null) }
    var fiatInput by remember { mutableStateOf("") }
    var cryptoInput by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    val conversionRate = selectedCrypto?.let {
        priceUpdates[it.id] ?: it.priceUsd.toDoubleOrNull()
    } ?: 1.0


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
                onSearchQueryChanged = { searchQuery = it },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val filteredList = if (searchQuery.isBlank()) {
                cryptoList.shuffled().take(3)
            } else {
                cryptoList.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            ) {
                items(filteredList) { crypto ->
                    val price = priceUpdates[crypto.id] ?: crypto.priceUsd.toDoubleOrNull() ?: 0.0
                    val formattedPrice = String.format("%.2f", price)

                    PortfolioItem(
                        crypto = crypto.name,
                        currentPrice = formattedPrice,
                        onClick = { selectedCrypto = crypto },
                        selected = selectedCrypto?.id == crypto.id,
                        showHint = true,
                        hintText = "Tap to buy",
                        compact = false
                    )
                }
            }

        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Buying ${selectedCrypto?.name}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        TextButton(onClick = { selectedCrypto = null }) {
                            Text("Change")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Enter the sum in USD:")
                    OutlinedTextField(
                        value = fiatInput,
                        onValueChange = { fiatInput = it },
                        label = { Text("Sum in USD") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("You will get this amount of ${selectedCrypto?.symbol}:")
                    StaticOutlinedTextFieldStyle(
                        label = stringResource(R.string.crypto_amount),
                        value = cryptoInput
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    CustomButton(
                        text = "Buy",
                        onClick = {
                            confirmedCrypto = selectedCrypto
                            showConfirmation = true
                        },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor = Color.White,
                        showBorder = false,
                        fontSize = 20.sp
                    )
                }
            }
        }

        if (showConfirmation) {
            AlertDialog(
                onDismissRequest = { showConfirmation = false },
                confirmButton = {
                    TextButton(onClick = {
                        val price = conversionRate
                        val quantity = cryptoInput.toDoubleOrNull() ?: 0.0
                        val id = confirmedCrypto?.id.orEmpty()
                        val name = confirmedCrypto?.name.orEmpty()

                        if (id.isNotEmpty() && name.isNotEmpty() && price > 0 && quantity > 0) {
                            tradeViewModel.executeTrade(
                                type = TradeType.BUY,
                                assetId = id,
                                assetName = name,
                                currentPrice = price,
                                quantity = quantity
                            )
                            showConfirmation = false
                            showSuccess = true
                        }
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmation = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Confirm purchase") },
                text = {
                    Text("Buy $cryptoInput ${confirmedCrypto?.symbol} for \$$fiatInput?")
                }
            )
        }

        if (showSuccess) {
            val scale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500),
                label = "scale"
            )

            AlertDialog(
                onDismissRequest = { showSuccess = false },
                confirmButton = {
                    TextButton(onClick = {
                        showSuccess = false
                        confirmedCrypto = null
                        selectedCrypto = null
                        fiatInput = ""
                        cryptoInput = ""
                    }) {
                        Text("OK")
                    }
                },
                title = { Text("Success!") },
                text = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.success_act),
                                contentDescription = "Success icon",
                                modifier = Modifier
                                    .size(200.dp)
                                    .scale(scale)
                                    .padding(bottom = 12.dp)
                            )
                            Text("Purchase successful!", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            )
        }
    }
}
