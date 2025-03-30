package com.example.traderapp.ui.screens.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.screens.components.texts.CustomTextField
import com.example.traderapp.ui.screens.components.texts.OnBoardingDescription
import com.example.traderapp.ui.screens.components.texts.QuestionText


import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun CreatePassword(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val password by authViewModel.password
    val confirmPassword by authViewModel.confirmPassword
    val touchIdEnabled by authViewModel.touchIdEnabled
    val isPasswordValid = authViewModel.isPasswordValid()
    val isConfirmPasswordValid = authViewModel.isConfirmPasswordValid()

    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                    authViewModel.resetFields()
                },
                logoResId = R.drawable.sl_bar4,
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
                    text = stringResource(R.string.create_a_password),
                    modifier = Modifier
                        .padding(top = 2.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OnBoardingDescription(
                        description = stringResource(R.string.about_password),
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

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

                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { authViewModel.onConfirmPasswordChange(it) },
                    label = "Confirm Password",
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

                if (!isConfirmPasswordValid) {
                    Text(
                        text = stringResource(R.string.passwords_do_not_match),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuestionText(question = stringResource(R.string.unlock_with_touch_id))
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
                Spacer(modifier = Modifier.height(38.dp))
                CustomButton(
                    text = stringResource(R.string.continuereg),
                    onClick = {navController.navigate("successful_registration") },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
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
