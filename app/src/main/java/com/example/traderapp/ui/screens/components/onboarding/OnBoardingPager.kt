package com.example.traderapp.ui.screens.components.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.traderapp.data.model.OnBoardingData

@Composable
fun OnBoardingPager(
    pagerState: androidx.compose.foundation.pager.PagerState,
    onBoardingData: List<OnBoardingData>
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()

    ) { page ->
        val item = onBoardingData[page]
        OnBoardingSlide(item)
    }
}
