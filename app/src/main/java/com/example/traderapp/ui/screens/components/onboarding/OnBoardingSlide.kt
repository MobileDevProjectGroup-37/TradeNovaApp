package com.example.traderapp.ui.screens.components.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.traderapp.data.model.OnBoardingData
import com.example.traderapp.R

@Composable
fun OnBoardingSlide(data: OnBoardingData) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = data.backgroundRes),
            contentDescription = "Slide Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .align(Alignment.TopCenter)
        )

        data.iconPositions.forEachIndexed { index, position ->
            val iconRes = when (index) {
                0 -> R.drawable.bitcoin
                1 -> R.drawable.ethereum
                2 -> R.drawable.solana
                else -> R.drawable.bitcoin
            }
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Icon $index",
                modifier = Modifier
                    .offset(x = position.first, y = position.second)
                    .size(40.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .zIndex(1f)
        ) {
            OnBoardingTitle(title = data.title)

            Spacer(modifier = Modifier.height(8.dp))
            OnBoardingDescription(description = data.description)
        }
    }
}

