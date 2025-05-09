package com.example.traderapp.ui.screens.trade

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@SuppressLint("DefaultLocale")
@Composable
fun BalanceHeader(balance: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Balance",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "$${String.format("%.2f", balance)}",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}
