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
import com.example.traderapp.data.model.SettingsItemData
import com.example.traderapp.data.model.toUserProfile
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

    var showQrDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    val userData by authViewModel.userData.collectAsState()
    val userProfile = userData?.toUserProfile()

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
                userProfile?.let {
                    UserProfileCard(userProfile = it)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                SettingsGroupCard(
                    title = "Security Settings",
                    items = listOf(
                        SettingsItemData(
                            iconRes = R.drawable.security_icon,
                            text = "Change Password",
                            onClick = { showPasswordDialog = true }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.history_icon,
                            text = "Login History",
                            onClick = { showLoginDialog = true }
                        )
                    )
                )
            }

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
                            onClick = { showNotificationDialog = true }
                        )
                    )
                )
            }

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
                            onClick = { showSupportDialog = true }
                        )
                    )
                )
            }

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

        if (showPasswordDialog) {
            AlertDialog(
                onDismissRequest = { showPasswordDialog = false },
                confirmButton = {
                    TextButton(onClick = { showPasswordDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Coming Soon!") },
                text = { Text("Password change is under development.") }
            )
        }

        if (showLoginDialog) {
            AlertDialog(
                onDismissRequest = { showLoginDialog = false },
                confirmButton = {
                    TextButton(onClick = { showLoginDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Coming Soon!") },
                text = { Text("Login history will be available soon.") }
            )
        }

        if (showNotificationDialog) {
            AlertDialog(
                onDismissRequest = { showNotificationDialog = false },
                confirmButton = {
                    TextButton(onClick = { showNotificationDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Coming Soon!") },
                text = { Text("Notification settings are coming soon.") }
            )
        }

        if (showQrDialog) {
            ShareQrDialog(
                onDismiss = { showQrDialog = false },
                qrText = "https://tradenova.app"
            )
        }

        if (showThemeDialog) {
            ThemeChooserDialog(
                currentTheme = themeViewModel.theme.collectAsState().value,
                onThemeSelected = {
                    themeViewModel.setTheme(it)
                },
                onDismiss = { showThemeDialog = false }
            )
        }

        if (showSupportDialog) {
            AlertDialog(
                onDismissRequest = { showSupportDialog = false },
                confirmButton = {
                    TextButton(onClick = { showSupportDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Support") },
                text = {
                    Column {
                        Text("✉️ Email: support@tradenova.app")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("🕐 Hours: Mon–Fri, 9:00–18:00")
                    }
                }
            )
        }
    }
}
