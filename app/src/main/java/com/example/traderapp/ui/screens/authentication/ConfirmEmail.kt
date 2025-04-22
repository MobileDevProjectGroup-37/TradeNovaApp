package com.example.traderapp.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun ConfirmMail(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                },
                logoResId = R.drawable.sl_bar2,
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
                    painter = painterResource(id = R.drawable.confirm_pic),
                    contentDescription = "Progress Bar Image",
                    modifier = Modifier
                        .size(400.dp)
                        .padding(16.dp)
                )
                AppTitle(
                    text = stringResource(R.string.confirm_email),
                    modifier = Modifier
                        .padding(top = 2.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.sent_email),
                    modifier = Modifier
                        .padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                CustomButton(
                    text = stringResource(R.string.confirm),
                    modifier = Modifier
                        .padding(top = 20.dp),
                    onClick = {
                        // Посмотрим что за email
                        println("DEBUG: email.value = ${authViewModel.email.value}")

                        authViewModel.sendOtp(
                            onSuccess = {
                                navController.navigate("enter_code")
                            },
                            onFailure = { error ->
                                println("Ошибка при отправке кода: $error")
                            }
                        )
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(R.string.I))
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append(stringResource(R.string.didn_t_receive))
                            }
                            append(stringResource(R.string.my_email))
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

        }
    )
}
