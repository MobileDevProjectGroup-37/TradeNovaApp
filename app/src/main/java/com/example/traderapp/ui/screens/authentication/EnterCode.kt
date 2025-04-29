package com.example.traderapp.ui.screens.authentication

import CodeInputField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.screens.components.texts.OnBoardingDescription

import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun EnterCode(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val email by authViewModel.email
    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                    authViewModel.resetFields()
                },
                logoResId = R.drawable.sl_bar3,
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

                AppTitle(
                    text = stringResource(R.string.please_enter_the_code),
                    modifier = Modifier
                        .padding(top = 2.dp),
                    textAlign = TextAlign.Center
                )
                OnBoardingDescription(
                    description = stringResource(R.string.sent_email, email),
                )
                Image(
                    painter = painterResource(id = R.drawable.envelop),
                    contentDescription = stringResource(R.string.envelop),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                )
                CodeInputField(
                    onCodeChange = { newCode ->
                        authViewModel.otpCode.value = newCode
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.didn_t_get_a_mail),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier)
                    TextButton(onClick = {}) {
                        Text(
                            text = stringResource(R.string.send_again),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                }
                CustomButton(
                    text = stringResource(R.string.entered),
                    onClick = {
                        println(">>> Attempting verifyOtp with code = ${authViewModel.otpCode.value}")
                        authViewModel.verifyOtp(
                            onSuccess = {
                                println(">>> verifyOtp success")
                                navController.navigate("create_password")
                            },
                            onFailure = { errorMsg ->

                                println("Error checking code: $errorMsg")
                            }
                        )
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

            }

        }
    )
}
