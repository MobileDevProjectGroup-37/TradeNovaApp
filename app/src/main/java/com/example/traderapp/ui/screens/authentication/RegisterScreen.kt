package com.example.traderapp.ui.screens.authentication

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.BackButtonWithLogo
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.CustomTextField
import com.example.traderapp.ui.screens.components.texts.DividerWithText
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.viewmodel.AuthViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
// Google Sign-In imports
import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.traderapp.utils.getGoogleSignInClient

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val email by authViewModel.email
    val password by authViewModel.password
    val validationError by authViewModel.validationError
    val isPasswordValid = authViewModel.isPasswordValid()

    // 🔑 Google Sign-In launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                authViewModel.signInWithGoogle(
                    account = account,
                    onSuccess = { navController.navigate("home") },
                    onFailure = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    TransparentStatusBar()

    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                    authViewModel.resetFields()
                },
                logoResId = R.drawable.logo_topbar,
                logoSize = 200.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
        ) {
            BackButtonWithLogo(navController)

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

            // 🔘 Google Sign-In Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomButton(
                    onClick = {
                        val googleSignInClient = getGoogleSignInClient(context)
                        launcher.launch(googleSignInClient.signInIntent)
                    },
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

            DividerWithText(" or signup with email ")

            Spacer(modifier = Modifier.height(24.dp))

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
                    buttonHeight = 55.dp
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
                TextButton(onClick = { navController.navigate("login") }) {
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
