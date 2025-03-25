package com.example.traderapp.viewmodel

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.traderapp.R
import com.example.traderapp.data.model.OnBoardingData

class OnBoardingViewModel : ViewModel() {

    val onBoardingData = listOf(
        OnBoardingData(
            title = "Take hold of your finances",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut eget mauris massa pharetra.",
            backgroundRes = R.drawable.onboarding1,
            iconPositions = listOf(
                Pair(10.dp, 110.dp),
                Pair(160.dp, 10.dp),
                Pair(300.dp, 110.dp)
            )
        ),
        OnBoardingData(
            title = "Smart trading tools",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut eget mauris massa pharetra.",
            backgroundRes = R.drawable.onboarding2,
            iconPositions = listOf(
                Pair(74.dp, 40.dp),
                Pair(140.dp, 6.dp),
                Pair(206.dp, 30.dp)
            )
        ),
        OnBoardingData(
            title = "Invest in the future",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut eget mauris massa pharetra.",
            backgroundRes = R.drawable.onboarding3,
            iconPositions = listOf(
                Pair(284.dp, 64.dp),
                Pair(24.dp,64.dp),
                Pair(150.dp, 10.dp)
            )
        ),
    )
}
