package com.theapache64.switchsnake.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Sulu = Color(0xffb7ee8e)

// Color set
val LightTheme = lightColors()
val DarkTheme = darkColors(
    primary = Sulu,
    primaryVariant = Color.Green
)

@Composable
fun SwitchSnakeTheme(
    isDark: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (isDark) DarkTheme else LightTheme
    ) {
        Surface {
            content()
        }
    }
}