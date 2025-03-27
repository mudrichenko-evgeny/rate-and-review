package com.mudrichenkoevgeny.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun Theme(
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    ratingColorScheme: RatingColorScheme = LightRatingColorScheme,
    content: @Composable() () -> Unit
) {
    CompositionLocalProvider(LocalRatingColorScheme provides ratingColorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = {
                content()
            }
        )
    }
}