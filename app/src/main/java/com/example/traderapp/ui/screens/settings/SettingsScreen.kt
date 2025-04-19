package com.example.traderapp.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.data.model.UserProfile
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.screens.components.cards.UserProfileCard
import com.example.traderapp.utils.Constants
import com.example.traderapp.viewmodel.AuthViewModel
import com.example.traderapp.viewmodel.ThemeViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    themeViewModel: ThemeViewModel
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Constants.LOGIN_SCREEN_ROUTE)
        } else {
            authViewModel.loadCurrentEmail()
        }
    }

    val email by authViewModel.email
    val userProfile = UserProfile(
        profileImageRes = R.drawable.profile_icon,
        userName = " ${email.substringBefore("@")}",
        userEmail = email,
        userId = "${email.hashCode()}",
        isVerified = authViewModel.isUserVerified()
    )

    var showQrDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.BACK,
                onBackClick = { navController.popBackStack() },
                onRightClick = {},
                title = "Settings"
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            item {
                UserProfileCard(userProfile = userProfile)
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Security Section
            item {
                SettingsGroupCard(
                    title = "Security Settings",
                    items = listOf(
                        SettingsItemData(
                            iconRes = R.drawable.security_icon,
                            text = "Change Password",
                            onClick = { /* TODO */ }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.history_icon,
                            text = "Login History",
                            onClick = { /* TODO */ }
                        )
                    )
                )
            }
            // Account Section
            item {
                SettingsGroupCard(
                    title = "Account",
                    items = listOf(
                        SettingsItemData(
                            iconRes = R.drawable.theme_icon,
                            text = "Theme",
                            onClick = { showThemeDialog = true }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.notification,
                            text = "Notification",
                            onClick = { /* TODO */ }
                        )
                    )
                )
            }
            // More Section
            item {
                SettingsGroupCard(
                    title = "More",
                    items = listOf(
                        SettingsItemData(
                            iconRes = R.drawable.share_icon,
                            text = "Share with friends",
                            onClick = { showQrDialog = true }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.question_icon,
                            text = "Support",
                            onClick = { /* TODO */ }
                        )
                    )
                )
            }

            // Logout
            item {
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    text = "Logout",
                    onClick = { authViewModel.logout() },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // QR Dialog
        if (showQrDialog) {
            ShareQrDialog(
                onDismiss = { showQrDialog = false },
                qrText = "https://tradenova.app"
            )
        }

        // Theme Dialog
        if (showThemeDialog) {
            ThemeChooserDialog(
                currentTheme = themeViewModel.theme.collectAsState().value,
                onThemeSelected = {
                    themeViewModel.setTheme(it)
                },
                onDismiss = { showThemeDialog = false }
            )
        }
    }
}
