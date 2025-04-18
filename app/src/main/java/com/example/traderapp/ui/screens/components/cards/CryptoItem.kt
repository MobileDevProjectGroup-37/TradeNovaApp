package com.example.traderapp.ui.screens.components.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traderapp.data.model.CryptoDto

@SuppressLint("DefaultLocale")
@Composable
fun CryptoItem(crypto: CryptoDto, currentPrice: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), shape = MaterialTheme.shapes.medium)
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(2.2f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = crypto.name, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val priceText = FormatTinyPrice(currentPrice)
                Text(text = priceText, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                val rawChange = crypto.changePercent24Hr ?: 0.0
                val formattedChange = String.format("%.2f", rawChange)
                val sign = if (rawChange >= 0) "+" else ""
                val color = if (rawChange >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

                Text(
                    text = "$sign$formattedChange%",
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun FormatTinyPrice(price: Double): AnnotatedString {
    val priceString = String.format("%.8f", price).trimEnd('0')
    val parts = priceString.split(".")

    return buildAnnotatedString {
        append("$${parts[0]}.")
        if (parts.size > 1) {
            val afterDot = parts[1]
            val leadingZeros = afterDot.takeWhile { it == '0' }
            val nonZeroPart = afterDot.drop(leadingZeros.length)

            if (afterDot.isEmpty()) {
                append("00")
            } else {
                withStyle(SpanStyle(fontSize = 10.sp)) {
                    append(leadingZeros)
                }
                append(if (nonZeroPart.isNotEmpty()) nonZeroPart else "00")
            }
        } else {
            append("00")
        }
    }
}
