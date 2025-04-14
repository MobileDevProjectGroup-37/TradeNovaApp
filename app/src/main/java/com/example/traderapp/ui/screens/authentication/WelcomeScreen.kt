package com.example.traderapp.ui.screens.authentication

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.bars.AppTopBar
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.buttons.GoogleSignInButton
import com.example.traderapp.ui.screens.components.texts.AppTitle
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.utils.getGoogleSignInClient
import com.example.traderapp.viewmodel.AuthViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignIn

@Composable
fun WelcomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current

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
                onBackClick = { navController.popBackStack() },
                logoResId = R.drawable.logo_topbar,
                logoSize = 200.dp
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AppTitle(
                    text = stringResource(R.string.welcome),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                GoogleSignInButton(
                    onClick = {
                        val googleSignInClient = getGoogleSignInClient(context)
                        launcher.launch(googleSignInClient.signInIntent)
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))

                CustomButton(
                    text = stringResource(R.string.sign_up_with_email),
                    onClick = { navController.navigate("account_creation") },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                )

                Spacer(modifier = Modifier.height(160.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.already_have_an_account2),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    text = stringResource(R.string.sign_in),
                    onClick = { navController.navigate("login") },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewWelcomeScreen() {
    val navController = rememberNavController()
    WelcomeScreen(navController)
}
