package com.example.traderapp.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.BackButtonRow
import com.example.traderapp.ui.screens.components.CustomButton
import com.example.traderapp.ui.theme.TraderAppTheme

@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onSignInClick: (String, String) -> Unit = { _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var touchIdEnabled by remember { mutableStateOf(false) }

    val isEmailValid =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1) Button "Back" + logo/name
            BackButtonRow(
                onBackClick = onBackClick
            )
            // TODO TRANSFER THOSE FIELDS TO COMPONENTS
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

            // 3) email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                label = { Text("Email address") },
                isError = email.isNotEmpty() && !isEmailValid,
                leadingIcon = { // Icon
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4) password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                label = { Text("Password") },
                isError = password.isNotEmpty() && !isPasswordValid,
                leadingIcon = { // 🔹 Иконка замка
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Password",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                visualTransformation = PasswordVisualTransformation(), // Пароль всегда скрыт
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 5) Forgot password?
            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(text = "Forgot your password? Click here")
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
                    onCheckedChange = { touchIdEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7) Button "Sign in" (use CustomButton)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CustomButton(
                    text = "Sign in",
                    onClick = { onSignInClick(email, password) },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    buttonWidth = 280.dp,
                    buttonHeight = 55.dp
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            // 8) Dividing line with "or continue with"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = " or ",
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

                //  Google Button (Outlined)
                CustomButton(
                    onClick = onGoogleClick,
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
                TextButton(onClick = onRegisterClick) {
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

@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    TraderAppTheme {
        LoginScreen()
    }
}
