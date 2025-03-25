package com.example.traderapp.ui.screens.components.texts

import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color

@Composable
fun ClickableText(
    text: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.primary
) {
    TextButton(onClick = onClick, modifier = Modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = textColor
            )
        )
    }
}
