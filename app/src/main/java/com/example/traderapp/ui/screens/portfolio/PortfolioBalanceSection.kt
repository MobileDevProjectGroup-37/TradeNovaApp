package com.example.traderapp.ui.screens.portfolio

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traderapp.ui.screens.components.texts.SubTitle

@SuppressLint("DefaultLocale")
@Composable
fun PortfolioBalanceSection(
    balance: Double,
    percentageChange: Double,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubTitle(
            text = "Portfolio Balance",
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = "$${String.format("%.2f", balance)}",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${String.format("%+.2f", percentageChange)}%",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text("Graph Placeholder", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
