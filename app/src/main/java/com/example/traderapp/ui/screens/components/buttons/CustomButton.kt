package com.example.traderapp.ui.screens.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Common reusable button component.
 *
 * @param text          The text on the button (if empty, only the icon will be shown)
 * @param onClick       The click event handler
 * @param modifier      An additional modifier
 * @param icon          The icon (if needed), positioned to the left of the text or filling the entire button
 * @param enabled       Indicates whether the button is enabled
 * @param isOutlined    If true, an OutlinedButton will be used; otherwise, a regular Button
 * @param buttonWidth   The width of the button
 * @param buttonHeight  The height of the button
 * @param fillImage     If true, the image will fill the entire button
 */

@Composable
fun CustomButton(
    text: String = "",
    onClick: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    icon: Painter? = null,
    fillImage: Boolean = false,
    rounded: Dp = 12.dp,
    paddingNeeded: Boolean = true,
    buttonWidth: Dp = Dp.Unspecified,
    buttonHeight: Dp = 60.dp,
    modifier: Modifier = Modifier,
    showBorder: Boolean = true,
    fontSize: TextUnit = 18.sp,
) {

    Box(
        modifier = modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .then(
                if (showBorder)
                    Modifier.border(2.dp, textColor, RoundedCornerShape(rounded))
                else
                    Modifier
            )
            .padding(0.dp)
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(rounded),
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            ),
            contentPadding = if (paddingNeeded) PaddingValues(16.dp) else PaddingValues(0.dp)
        ) {
            if (icon != null && fillImage) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    icon?.let {
                        Icon(
                            painter = it,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = Color.Unspecified
                        )
                    }
                    Text(
                        text = text,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = fontSize
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

