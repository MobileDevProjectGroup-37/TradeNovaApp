package com.example.traderapp.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun SuccessfulRegistration(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                    authViewModel.resetFields()
                },
                logoResId = R.drawable.sl_bar5,
                logoSize = 200.dp
            )
            Spacer(modifier = Modifier.height(100.dp))
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.successful_reg),
                    contentDescription = stringResource(R.string.successful_registration),
                    modifier = Modifier
                        .size(300.dp)
                        .padding(16.dp)
                )
                AppTitle(
                    text = stringResource(R.string.verification_success),
                    modifier = Modifier
                        .padding(top = 20.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.congrats),
                    modifier = Modifier
                        .padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                CustomButton(
                    text = stringResource(R.string.start_now),
                    modifier = Modifier
                        .padding(top = 80.dp),
                    onClick = { navController.navigate("login") },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,

                    )
            }

        }
    )
}