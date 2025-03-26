package com.example.traderapp.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.BackButtonWithLogo
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.CustomTextField
import com.example.traderapp.ui.screens.components.texts.DividerWithText
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val email = authViewModel.email.value
    val password = authViewModel.password.value
    val validationError = authViewModel.validationError.value
    val isPasswordValid = authViewModel.isPasswordValid()

    TransparentStatusBar()
    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                    authViewModel.resetFields() // Reset the fields here
                },
                logoResId = R.drawable.logo_topbar,
                logoSize = 200.dp
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text("The content")
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            BackButtonWithLogo(navController)
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
                    onClick = { /* logic*/ },
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
                customTrailingIcon = R.drawable.ic_check
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6) Password
            CustomTextField(
                value = password,
                onValueChange = { authViewModel.onPasswordChange(it) },
                label = "Password",
                isValid = isPasswordValid,
                isPassword = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.lock_light),
                        contentDescription = "Password Icon",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Unspecified
                    )
                },
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
                    onClick = {
                        authViewModel.register(
                            onSuccess = { navController.navigate("home") },
                            onFailure = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    buttonWidth = 280.dp,
                    buttonHeight = 55.dp,
                )
            }

            validationError?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
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
                TextButton(onClick = { /* logic for SignIn*/ }) {
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

