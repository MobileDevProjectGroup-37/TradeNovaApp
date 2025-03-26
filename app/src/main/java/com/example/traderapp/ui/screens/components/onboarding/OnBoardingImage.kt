package com.example.traderapp.ui.screens.components.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun OnBoardingImage(imageRes: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = modifier
            .width(350.dp)
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Fit

    )
}
