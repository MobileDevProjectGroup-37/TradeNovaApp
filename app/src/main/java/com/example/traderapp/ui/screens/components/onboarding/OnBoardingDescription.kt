package com.example.traderapp.ui.screens.components.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun OnBoardingDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
        modifier = Modifier.padding(horizontal = 24.dp),

        )
}
