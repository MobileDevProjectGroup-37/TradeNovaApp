package com.example.traderapp.ui.screens.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun OnBoardingDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()

    )
}