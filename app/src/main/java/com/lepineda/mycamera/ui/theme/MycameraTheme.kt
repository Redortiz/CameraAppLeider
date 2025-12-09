package com.lepineda.mycamera.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun MycameraTheme(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.background,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        // Aquí usamos el modifier y el color que le pasas en MainActivity
        Surface(
            modifier = modifier,
            color = color
        ) {
            content()
        }
    }
}