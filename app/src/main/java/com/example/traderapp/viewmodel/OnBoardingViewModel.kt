package com.example.traderapp.viewmodel



import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.traderapp.R
import com.example.traderapp.data.model.OnBoardingData


class OnBoardingViewModel : ViewModel() {

    var pagerState by mutableStateOf(0) // current page
        private set

    // Data for each onboarding slide
    val onBoardingData = listOf(
        OnBoardingData("Take hold of your finances", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut eget mauris massa pharetra.", R.drawable.onboarding1),
        OnBoardingData("Smart trading tools", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut eget mauris massa pharetra.", R.drawable.onboarding2),
        OnBoardingData("Invest in the future", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut eget mauris massa pharetra.", R.drawable.onboarding3)
    )

    // Method to update the current page index
    fun updatePage(page: Int) {
        pagerState = page
    }
}