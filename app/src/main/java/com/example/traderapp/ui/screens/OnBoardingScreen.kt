package com.example.traderapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.ui.screens.components.onboarding.OnBoardingDots
import com.example.traderapp.ui.screens.components.onboarding.OnBoardingPager
import com.example.traderapp.viewmodel.OnBoardingViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun OnBoardingScreen(navController: NavController, viewModel: OnBoardingViewModel) {

    val pagerState = rememberPagerState(pageCount = { viewModel.onBoardingData.size })
    var currentPage by remember { mutableStateOf(0) } // 1st page

    LaunchedEffect(pagerState) { // function in JC which helps with async
        // when the pager state changes, it creates a flow to renew the page
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            currentPage = page
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,


        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // make it beautiful
            ) {
                OnBoardingPager(pagerState, viewModel.onBoardingData)
            }
            OnBoardingDots(currentPage = currentPage, pageCount = viewModel.onBoardingData.size)

            Spacer(modifier = Modifier.height(24.dp))


            Spacer(modifier = Modifier.height(44.dp))

            Button(
                onClick = {
                    navController.navigate("welcome")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
    }


