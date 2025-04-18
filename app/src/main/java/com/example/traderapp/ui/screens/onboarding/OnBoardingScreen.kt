package com.example.traderapp.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.viewmodel.OnBoardingViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.res.stringResource
import com.example.traderapp.ui.theme.TransparentStatusBar

@Composable
fun OnBoardingScreen(navController: NavController, viewModel: OnBoardingViewModel) {
    val pagerState = rememberPagerState(pageCount = { viewModel.onBoardingData.size })
    var currentPage by remember { mutableIntStateOf(0) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            currentPage = page
        }
    }
    TransparentStatusBar()
    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = false,
                onBackClick = { navController.popBackStack() },
                logoResId = R.drawable.logo_topbar,
                logoSize = 200.dp
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        OnBoardingPager(
                            pagerState = pagerState,
                            onBoardingData = viewModel.onBoardingData
                        )
                    }
                    OnBoardingDots(currentPage = currentPage, pageCount = viewModel.onBoardingData.size)

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomButton(
                        text = stringResource(id = R.string.continue_text),
                        onClick = { navController.navigate("welcome") },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        rounded = 16.dp,
                        buttonWidth = Dp.Unspecified,
                        buttonHeight = 60.dp,
                        paddingNeeded = true
                    )
                }
            }
        }
    )
}
