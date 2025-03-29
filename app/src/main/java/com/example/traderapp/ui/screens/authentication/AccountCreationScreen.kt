package com.example.traderapp.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.bars.signupBar.ProgressBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.AppTitle

@Composable
fun AccountCreationScreen(navController: NavController) {

    val steps = listOf(
        stringResource(R.string.create_your_account),
        stringResource(R.string.confirm_your_email),
        stringResource(R.string.create_a_password)
    )

    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                AppTitle(
                    text = stringResource(R.string.get_started_in_3_easy_steps),
                    modifier = Modifier
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = painterResource(id = R.drawable.account_creation),
                    contentDescription = "Progress Bar Image",
                    modifier = Modifier
                        .size(280.dp)
                        .padding(16.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 90.dp, end = 16.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ProgressBar(steps = steps)
                }

                CustomButton(
                    text = stringResource(R.string.continuereg),
                    onClick = { navController.navigate("login") },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
        }
    )
}


