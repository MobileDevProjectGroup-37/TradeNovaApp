package com.example.traderapp.ui.screens.components.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.traderapp.data.model.OnBoardingData

@Composable
fun OnBoardingSlide(data: OnBoardingData) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OnBoardingImage(imageRes = data.imageRes)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBoardingTitle(title = data.title)
                OnBoardingDescription(description = data.description)
            }
        }
    }
}
