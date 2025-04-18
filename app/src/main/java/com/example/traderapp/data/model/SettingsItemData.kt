package com.example.traderapp.data.model

data class SettingsItemData(
    val iconRes: Int,
    val text: String,
    val onClick: () -> Unit
)
