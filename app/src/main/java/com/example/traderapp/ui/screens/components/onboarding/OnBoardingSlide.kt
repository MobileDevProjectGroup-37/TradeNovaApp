package com.example.traderapp.ui.screens.components.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.traderapp.data.model.OnBoardingData


@Composable
fun OnBoardingSlide(data: OnBoardingData) {
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OnBoardingImage(imageRes = data.imageRes)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                OnBoardingTitle(title = data.title)

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            OnBoardingDescription(description = data.description)
        }
    }
}


