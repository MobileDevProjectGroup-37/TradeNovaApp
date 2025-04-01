package com.example.traderapp.ui.screens.authentication

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.buttons.GoogleSignInButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.screens.components.texts.CustomTextField
import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun ResetPasswordScreen(
    navController: NavController? = null,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var errorMessage by remember { mutableStateOf("") }
    val email by authViewModel.email

    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = { navController?.popBackStack() },
                logoResId = R.drawable.logo_topbar,
                logoSize = 200.dp
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AppTitle(
                    text = stringResource(R.string.password_reset),
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.password_reset_instruction),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                CustomTextField(
                    value = email,
                    onValueChange = { authViewModel.onEmailChange(it) },
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(190.dp))

//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 160.dp),
//                    contentAlignment = Alignment.Center
//                ) {
                    CustomButton(
                        text = stringResource(R.string.continue_text),
                        onClick = {
                            authViewModel.resetPassword(
                                onSuccess = {
                                    navController?.navigate("confirm_mail")
                                },
                                onFailure = { error ->
                                    errorMessage = error
                                }
                            )
                        },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = stringResource(R.string.terms_and_cons),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.or),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GoogleSignInButton(
                        onClick = { navController?.navigate("forgot_password") }
                    )

                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

        }
    )
}








