package com.example.traderapp.ui.screens.authentication

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.screens.components.texts.CustomTextField
import com.example.traderapp.ui.screens.components.texts.OnBoardingDescription
import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun AddMail(
    navController: NavController,
    authViewModel: AuthViewModel //
) {
    val email by authViewModel.email

    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = {

                    navController.popBackStack()
                },
                logoResId = R.drawable.sl_bar1,
                logoSize = 200.dp
            )
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
                    text = stringResource(R.string.what_s_your_email),
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                OnBoardingDescription(
                    description = stringResource(R.string.enter_email_description)
                )
                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = email,
                    onValueChange = {
                        authViewModel.onEmailChange(it)
                    },
                    label = "Email address",
                    isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                    leadingIcon = { size ->
                        Icon(
                            painter = painterResource(id = R.drawable.mail_icon),
                            contentDescription = "Email Icon",
                            modifier = Modifier.size(size),
                            tint = Color.Unspecified
                        )
                    },
                    customTrailingIcon = R.drawable.ic_check
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.already_have_an_account),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text(
                            text = stringResource(R.string.sign_in),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))

                CustomButton(
                    text = stringResource(R.string.continuereg),
                    onClick = {
                        // Просто навигируем дальше
                        navController.navigate("confirm_mail")
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(38.dp))
                OnBoardingDescription(
                    description = stringResource(R.string.terms_and_cons)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}
