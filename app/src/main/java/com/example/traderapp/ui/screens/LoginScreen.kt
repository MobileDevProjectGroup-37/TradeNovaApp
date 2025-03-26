package com.example.traderapp.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.screens.components.texts.CustomTextField
import com.example.traderapp.ui.screens.components.texts.QuestionText
import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var errorMessage by remember { mutableStateOf("") }
    val email by authViewModel.email
    val password by authViewModel.password
    val isPasswordValid = authViewModel.isPasswordValid()
    val touchIdEnabled by authViewModel.touchIdEnabled

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AppTitle(
                    text = "Login to your\nAccount",
                    modifier = Modifier.padding(16.dp),
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuestionText(question = "Unlock with Touch ID?")
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
                                    navController.navigate("home")
                                },
                                onFailure = { error ->
                                    errorMessage = error
                                }
                            )
                        },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor = Color.White
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
        }
    )
}
