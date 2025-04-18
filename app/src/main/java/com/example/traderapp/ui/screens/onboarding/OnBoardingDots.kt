package com.example.traderapp.ui.screens.onboarding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OnBoardingDots(currentPage: Int, pageCount: Int) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { iteration ->
            val color = if (currentPage == iteration) MaterialTheme.colorScheme.primary else Color(0xFFA6A6A6)

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .width(if (currentPage == iteration) 30.dp else 20.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(60))
                    .background(color)
            )
        }
    }
}
