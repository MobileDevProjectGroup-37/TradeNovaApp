package com.example.traderapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.BackButtonRow
import com.example.traderapp.ui.screens.components.CustomButton
import com.example.traderapp.ui.screens.components.CustomTextField
import com.example.traderapp.ui.screens.components.DividerWithText
import com.example.traderapp.ui.theme.TraderAppTheme

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onSignUpWithEmail: (String, String) -> Unit = { _, _ -> },
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
    onSignInClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val isEmailValid = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            BackButtonRow(
                onBackClick = onBackClick
            )

            // 2) header
            Text(
                text = "Hello! Start your\ncrypto investment today",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 3) Google-button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
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

            // 4) Dividing line "or continue with"
            DividerWithText(" or signup with email ")

            Spacer(modifier = Modifier.height(24.dp))

            // 5) Email
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email address",
                isValid = isEmailValid,
                leadingIcon = Icons.Filled.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6) Password
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isValid = isPasswordValid,
                isPassword = true,
                leadingIcon = Icons.Filled.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 7) Button "Sign up with email"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CustomButton(
                    text = "Sign up with email",
                    onClick = { onSignUpWithEmail(email, password) },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    buttonWidth = 280.dp,
                    buttonHeight = 55.dp,
                    //enabled = isEmailValid && isPasswordValid
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 8) Do u have an account? Sign In
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = onSignInClick) {
                    Text(
                        text = "Sign In",
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
fun PreviewRegisterScreen() {
    TraderAppTheme {
        RegisterScreen()
    }
}
