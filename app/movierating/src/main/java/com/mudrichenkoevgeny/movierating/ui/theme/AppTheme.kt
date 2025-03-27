package com.mudrichenkoevgeny.movierating.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.mudrichenkoevgeny.core.ui.theme.Theme

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (!useDarkTheme) LightColorScheme else DarkColorScheme
    val ratingColorScheme = if (!useDarkTheme) LightRatingColorScheme else DarkRatingColorScheme

    Theme(
        colorScheme = colorScheme,
        ratingColorScheme = ratingColorScheme,
        content = content
    )
}