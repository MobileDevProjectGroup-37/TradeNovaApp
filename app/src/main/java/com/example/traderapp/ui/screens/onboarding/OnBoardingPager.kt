package com.example.traderapp.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.traderapp.data.model.OnBoardingData

@Composable
fun OnBoardingPager(
    pagerState: PagerState,
    onBoardingData: List<OnBoardingData>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val currentPageData = onBoardingData[page]
            OnBoardingSlide(data = currentPageData)
        }
    }
}
