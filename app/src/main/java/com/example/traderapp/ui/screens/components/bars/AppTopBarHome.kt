package com.example.traderapp.ui.screens.components.bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.traderapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBarHome(
    navigationIconType: NavigationIconType = NavigationIconType.BACK,
    rightIconType: RightIconType = RightIconType.NONE,
    onBackClick: (() -> Unit)? = null,
    onRightClick: (() -> Unit)? = null,
    title: String? = null,
    logoResId: Int? = null,
    logoSize: Dp = 30.dp,
    topBarColor: Color = MaterialTheme.colorScheme.background
) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (logoResId != null) {
                    Image(
                        painter = painterResource(id = logoResId),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(logoSize)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                title?.let {
                    Text(text = it, textAlign = TextAlign.Center)
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = topBarColor
        ),
        navigationIcon = {
            when (navigationIconType) {
                NavigationIconType.BACK -> {
                    IconButton(onClick = onBackClick ?: {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.custom_arrow_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                NavigationIconType.PROFILE -> {
                    IconButton(onClick = onBackClick ?: {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_icon),
                            contentDescription = "Profile",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                NavigationIconType.SETTINGS -> {
                    IconButton(onClick = onBackClick ?: {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.setting_icon),
                            contentDescription = "Settings",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        },
        actions = {
            when (rightIconType) {
                RightIconType.NONE -> { /* No icons */ }
                RightIconType.SETTINGS -> {
                    IconButton(onClick = onRightClick ?: {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.setting_icon),
                            contentDescription = "Settings",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                RightIconType.TRADE -> {
                    IconButton(onClick = onRightClick ?: {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.trade_icon),
                            contentDescription = "Trade",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                RightIconType.NOTIFICATIONS -> {
                    IconButton(onClick = onRightClick ?: {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.mail_icon),
                            contentDescription = "Notifications",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                RightIconType.UNION -> {
                    IconButton(onClick = onRightClick ?: {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.union_icon),
                            contentDescription = "Notifications",
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

            }
        },
        modifier = Modifier.height(64.dp)
    )
}


enum class NavigationIconType {
    BACK,
    SETTINGS,
    PROFILE
}


enum class RightIconType {
    NONE,
    SETTINGS,
   TRADE,
    NOTIFICATIONS,
    UNION
}
