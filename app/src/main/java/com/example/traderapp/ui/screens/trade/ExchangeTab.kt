package com.example.traderapp.ui.screens.trade

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.PortfolioBalanceSection
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.CryptoViewModel
import com.example.traderapp.viewmodel.TradeViewModel

@SuppressLint("DefaultLocale")
@Composable
fun ExchangeTab(
    navController: NavController,
    tradeViewModel: TradeViewModel,
    cryptoViewModel: CryptoViewModel
) {
    val cryptoList by cryptoViewModel.cryptoList.collectAsState()
    val priceUpdates by cryptoViewModel.priceUpdates.collectAsState()

    if (cryptoList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var selectedCryptoFrom by remember { mutableStateOf(cryptoList.getOrNull(0)) }
    var selectedCryptoTo by remember { mutableStateOf(cryptoList.getOrNull(1)) }
    var cryptoInput by remember { mutableStateOf("") }
    var fiatInput by remember { mutableStateOf("") }
    var lastChanged by remember { mutableStateOf("crypto") }

    val conversionRateFrom = selectedCryptoFrom?.let {
        priceUpdates[it.id] ?: it.priceUsd.toDoubleOrNull()
    } ?: 1.0
    val conversionRateTo = selectedCryptoTo?.let {
        priceUpdates[it.id] ?: it.priceUsd.toDoubleOrNull()
    } ?: 1.0

    LaunchedEffect(cryptoInput, selectedCryptoFrom) {
        val cryptoDouble = cryptoInput.toDoubleOrNull()
        if (cryptoDouble != null && conversionRateFrom > 0) {
            val usd = cryptoDouble * conversionRateFrom
            fiatInput = String.format("%.2f", usd)

            println("🧮 [ExchangeTab] $cryptoDouble ${selectedCryptoFrom?.symbol} = $usd USD @ rate $conversionRateFrom")
        } else {
            println("⚠️ [ExchangeTab] Invalid conversion rate or input: $cryptoInput @ $conversionRateFrom")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CurrencySelector(
                selected = selectedCryptoFrom?.name ?: "Choose",
                options = cryptoList.map { it.name },
                onSelect = { name -> selectedCryptoFrom = cryptoList.find { it.name == name } },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    val temp = selectedCryptoFrom
                    selectedCryptoFrom = selectedCryptoTo
                    selectedCryptoTo = temp
                },
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.exchange_icon),
                    contentDescription = "Exchange",
                    modifier = Modifier.size(30.dp)
                )
            }

            CurrencySelector(
                selected = selectedCryptoTo?.name ?: "Choose",
                options = cryptoList.map { it.name },
                onSelect = { name -> selectedCryptoTo = cryptoList.find { it.name == name } },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.enter_the_amount_of_crypto))
        OutlinedTextField(
            value = cryptoInput,
            onValueChange = {
                cryptoInput = it
                lastChanged = "crypto"
            },
            label = { Text(stringResource(R.string.the_amount)) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = cryptoInput.toDoubleOrNull() == null,
            supportingText = {
                if (cryptoInput.toDoubleOrNull() == null) {
                    Text(stringResource(R.string.enter_a_valid_number))
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cryptoInput,
            onValueChange = { cryptoInput = it },
            label = { Text(stringResource(R.string.the_amount_in_usd))},
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(12.dp)),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )

        )

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(
            text = stringResource(R.string.exchange),
            onClick = {
                val amount = cryptoInput.toDoubleOrNull()
                if (selectedCryptoFrom != null && selectedCryptoTo != null && amount != null) {
                    tradeViewModel.executeExchange(
                        fromAssetId = selectedCryptoFrom!!.id,
                        toAssetId = selectedCryptoTo!!.id,
                        fromAmount = amount,
                        priceMap = priceUpdates,

                    )
                }
            },
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = Color.White,
            showBorder = false,
            fontSize = 20.sp
        )

    }
}
