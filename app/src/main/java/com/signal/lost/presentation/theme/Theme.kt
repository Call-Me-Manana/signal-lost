package com.signal.lost.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SignalLostColors = darkColorScheme(
    primary = Color(0xFF8BE9FD),
    secondary = Color(0xFFB8D8E0),
    background = Color(0xFF071013),
    surface = Color(0xFF0E1A1F),
    onPrimary = Color(0xFF071013),
    onSecondary = Color(0xFF071013),
    onBackground = Color(0xFFE6F7FF),
    onSurface = Color(0xFFE6F7FF)
)

@Composable
fun SignalLostTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SignalLostColors,
        content = content
    )
}
