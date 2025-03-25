package com.example.traderapp.data.model
import androidx.compose.ui.unit.Dp

// this  model shows what is inside each slide)
data class OnBoardingData(
    val title: String,
    val description: String,
    val backgroundRes: Int,
    val iconPositions: List<Pair<Dp, Dp>> // positions of icons
)
