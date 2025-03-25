package com.example.traderapp.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.texts.ClickableText
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.CustomTextField
import com.example.traderapp.ui.screens.components.texts.QuestionText
import com.example.traderapp.ui.screens.components.buttons.SquareButton
import com.example.traderapp.ui.theme.TraderAppTheme
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.viewmodel.AuthViewModel


@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel){

    val email by authViewModel.email
    val password by authViewModel.password
    val touchIdEnabled by authViewModel.touchIdEnabled
    val validationError = authViewModel.validationError.value

    var errorMessage by remember { mutableStateOf("") }

    TransparentStatusBar()

    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
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
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // 2) Header
                    AppTitle(
                        text = "Login to your\nAccount",
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )

                    // 3) Email field
                    CustomTextField(
                        value = email,
                        onValueChange = { authViewModel.onEmailChange(it) },
                        label = "Email address",
                        isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                        leadingIcon = Icons.Filled.Email,
                        customTrailingIcon = R.drawable.ic_check,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4) Password field
                    CustomTextField(
                        value = password,
                        onValueChange = { authViewModel.onPasswordChange(it) },
                        label = "Password",
                        isValid = password.length >= 8,
                        isPassword = true,
                        leadingIcon = Icons.Filled.Lock,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    // 5) Forgot password?
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuestionText(
                            question = "Forgot your password?"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        ClickableText(
                            text = "Click here",
                            onClick = {
                                /* logic*/
                                navController.navigate("forgot_password")
                            }
                        )
                    }
// Display validation error message if any
                    if (!validationError.isNullOrEmpty()) {
                        Text(
                            text = validationError,
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
// Touch ID toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuestionText(
                            question = "Unlock with Touch ID?"
                        )
                        Switch(
                            checked = touchIdEnabled,
                            onCheckedChange = { authViewModel.onTouchIdChange(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }

                    // 7) Sign in button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomButton(
                            text = "Sign in",
                            onClick = {
                                authViewModel.login(
                                    onSuccess = {
                                        navController.navigate("home") // go to home page
                                    },
                                    onFailure = { error ->
                                        errorMessage = error
                                    }
                                )
                            },
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            textColor = Color.White,

                            )
                    }
                    // Display login error message if login fails
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

                    Spacer(modifier = Modifier.height(100.dp))

                    // 8) Dividing line with "or continue with"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            text = " or continue with ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SquareButton(
                            onClick = {
                                /* logic */
                            },
                            iconResId = R.drawable.google_logo1,
                            buttonSize = 48.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(44.dp))

                    // 10) "Don't have an account? Register"

                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    TraderAppTheme {
        val navController = rememberNavController()
        val authViewModel: AuthViewModel = viewModel()
        LoginScreen(navController = navController, authViewModel = authViewModel)
    }
}