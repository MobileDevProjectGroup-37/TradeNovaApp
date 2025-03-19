package com.example.traderapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/********************************
 *  Цветовые константы (Light)
 ********************************/
val LightBackground = Color(0xFFF8F9FC)  // #F8F9FC
val LightSurface    = Color(0xFFFFFFFF) // #FFFFFF
val LightOffline    = Color(0xFFD7D9E4) // #D7D9E4
val LightSecondary  = Color(0xFF696F8C) // #696F8C
val LightText       = Color(0xFF11183C) // #11183C
val LightPrimary    = Color(0xFF5DDC71) // #5DDC71
val LightUp         = Color(0xFF098C26)
val LightFall       = Color(0xFFF7931A)
val LightError      = Color(0xFFCD0000)

/********************************
 *  Цветовые константы (Dark)
 ********************************/
val DarkBackground = Color(0xFF0D0D16)  // #0D0D16
val DarkSurface    = Color(0xFF22283A) // #22283A
val DarkOffline    = Color(0xFF3D455C) // #3D455C
val DarkSecondary  = Color(0xFF9096A2) // #9096A2
val DarkText       = Color(0xFFFFFFFF) // #FFFFFF (прозрачность при желании)
val DarkPrimary    = Color(0xFF5DDC71) // #5DDC71
val DarkUp         = Color(0xFF31C451)
val DarkFall       = Color(0xFFF89E32)
val DarkError      = Color(0xFFFF6666)

/********************************
 *   Light / Dark палитры
 ********************************/
private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    secondary = LightSecondary,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = LightText,
    surface = LightSurface,
    onSurface = LightText,
    error = LightError,
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.White,
    secondary = DarkSecondary,
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    error = DarkError,
    onError = Color.White,
)

/********************************
 *   Типографика (пример)
 ********************************/
val TradeNovaTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        fontFamily = FontFamily.SansSerif
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily.SansSerif
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        fontFamily = FontFamily.SansSerif
    )
    // При необходимости добавьте другие стили
)

/********************************
 *   Тема (Composable)
 ********************************/
@Composable
fun TraderAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = TradeNovaTypography,
        content = content
    )
}
