package com.example.traderapp.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.traderapp.R
import com.example.traderapp.ui.screens.components.cards.SettingsCard
import com.example.traderapp.ui.screens.components.cards.SettingsItemData
import com.example.traderapp.data.model.UserProfile
import com.example.traderapp.ui.screens.components.cards.UserProfileCard
import com.example.traderapp.ui.screens.components.bars.AppTopBarHome
import com.example.traderapp.ui.screens.components.bars.BottomNavigationBar
import com.example.traderapp.ui.screens.components.bars.NavigationIconType
import com.example.traderapp.ui.screens.components.bars.RightIconType
import com.example.traderapp.ui.screens.components.buttons.CustomButton
import com.example.traderapp.ui.theme.TransparentStatusBar
import com.example.traderapp.utils.Constants // Импортируем Constants
import com.example.traderapp.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    TransparentStatusBar()

    // Отслеживаем isLoggedIn
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Добавляем LaunchedEffect для навигации
    LaunchedEffect(key1 = isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Constants.LOGIN_SCREEN_ROUTE) // Используем константу
        }
    }

    val userProfile = UserProfile(
        profileImageRes = R.drawable.profile_icon,
        userName = "John Doe",
        userEmail = "johndoe@example.com",
        userId = "12345",
        isVerified = true
    )

    Scaffold(
        topBar = {
            AppTopBarHome(
                navigationIconType = NavigationIconType.BACK,
                rightIconType = RightIconType.SEARCH,
                onBackClick = { navController.popBackStack() },
                onRightClick = { /* action */ },
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
                .padding(16.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                UserProfileCard(userProfile = userProfile)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(text = "Security Settings", fontSize = 20.sp, color = Color.Black)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                SettingsCard(
                    items = listOf(
                        SettingsItemData(
                            iconRes = R.drawable.profile_icon,
                            text = "Profile",
                            onClick = { /* Handle click for Privacy */ }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.security_icon,
                            text = "Security",
                            onClick = { /* Handle click for Security */ }
                        )
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(text = "Finance", fontSize = 20.sp, color = Color.Black)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                SettingsCard(
                    items = listOf(
                        SettingsItemData(
                            iconRes = R.drawable.history_icon,
                            text = "History",
                            onClick = { /* Handle click for Privacy */ }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.limits_icon,
                            text = "Limits",
                            onClick = { /* Handle click for Security */ }
                        )
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(text = "Account", fontSize = 20.sp, color = Color.Black)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                SettingsCard(
                    items = listOf(
                        SettingsItemData(
                            iconRes = R.drawable.theme_icon,
                            text = "Theme",
                            onClick = { /* Handle click for Privacy */ }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.notification,
                            text = "Notification",
                            onClick = { /* Handle click for Security */ }
                        )
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(text = "More", fontSize = 20.sp, color = Color.Black)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                SettingsCard(
                    items = listOf(
                        SettingsItemData(
                            iconRes = R.drawable.bonus_icon,
                            text = "My bonus",
                            onClick = { /* Handle click for Privacy */ }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.share_icon,
                            text = "Share with friends",
                            onClick = { /* Handle click for Security */ }
                        ),
                        SettingsItemData(
                            iconRes = R.drawable.question_icon,
                            text = "Support",
                            onClick = { /* Handle click for Security */ }
                        )
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                CustomButton(
                    text = "Logout",
                    onClick = {
                        authViewModel.logout() // Вызываем authViewModel.logout()
                    },
                    backgroundColor = Color.White,
                    textColor = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}