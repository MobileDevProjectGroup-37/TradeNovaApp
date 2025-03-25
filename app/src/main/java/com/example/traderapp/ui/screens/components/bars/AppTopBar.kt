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
fun AppTopBar(

    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    logoResId: Int? = null,
    title: String? = null,
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
            if (showBackButton && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.custom_arrow_back),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        modifier = Modifier.height(64.dp)
    )
}
