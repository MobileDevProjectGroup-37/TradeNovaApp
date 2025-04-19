package com.example.traderapp.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.traderapp.ui.screens.portfolio.SettingsItem


@Composable
fun SettingsCard(items: List<SettingsItemData>) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            items.forEachIndexed { index, item ->
                SettingsItem(iconRes = item.iconRes, text = item.text, onClick = item.onClick)
                if (index < items.size - 1) {
                    Divider()
                }
            }
        }
    }
}

data class SettingsItemData(
    val iconRes: Int,
    val text: String,
    val onClick: () -> Unit
)
