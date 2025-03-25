package com.example.traderapp.ui.screens.components.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.OnBoardingData

@Composable
fun OnBoardingSlide(data: OnBoardingData) {
    Column(
        modifier = Modifier
            .fillMaxSize(),


        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OnBoardingImage(imageRes = data.imageRes)
        OnBoardingTitle(title = data.title)
        Spacer(modifier = Modifier.height(8.dp))

        OnBoardingDescription(description = data.description)
    }
}
