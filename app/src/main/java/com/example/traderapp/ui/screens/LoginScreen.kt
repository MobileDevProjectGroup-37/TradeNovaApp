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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.AppTopBar
import com.example.traderapp.ui.screens.components.CustomButton
import com.example.traderapp.ui.screens.components.CustomTextField
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
                    Text(
                        text = "Login to your\nAccount",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // 3) Email field
                    CustomTextField(
                        value = email,
                        onValueChange = { authViewModel.onEmailChange(it) },
                        label = "Email address",
                        isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                        leadingIcon = Icons.Filled.Email,
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // 5) Forgot password?
                    TextButton(
                        onClick = { navController.navigate("forgot_password") },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text(text = "Forgot your password? Click here")
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

                    // 6) Touch ID toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Unlock with Touch ID?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = touchIdEnabled,
                            onCheckedChange = { authViewModel.onTouchIdChange(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
                            buttonWidth = 280.dp,
                            buttonHeight = 55.dp
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

                    Spacer(modifier = Modifier.height(24.dp))

                    // 8) Dividing line with "or continue with"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f))
                        Text(
                            text = " or ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Divider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Google Button (Outlined)
                        CustomButton(
                            onClick = { /* Implement Google login logic here */ },
                            backgroundColor = Color.White,
                            textColor = Color.Black,
                            icon = painterResource(id = R.drawable.google_logo),
                            fillImage = true,
                            rounded = 8.dp,
                            paddingNeeded = false,
                            buttonWidth = 232.dp,
                            buttonHeight = 48.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 10) "Don't have an account? Register"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = { navController.navigate("register") }) {
                            Text(
                                text = "Register",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
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
