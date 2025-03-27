package com.mudrichenkoevgeny.movierating.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.mudrichenkoevgeny.core.ui.theme.RatingColorScheme

val LightColorScheme: ColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = LightOnPrimary,
    secondary = SecondaryColor,
    onSecondary = LightOnSecondary,
    tertiary = TertiaryColor,
    onTertiary = LightOnTertiary,
    background = LightBackground,
    surface = LightSurface,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Dark Theme
val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = DarkOnPrimary,
    secondary = SecondaryColor,
    onSecondary = DarkOnSecondary,
    tertiary = TertiaryColor,
    onTertiary = DarkOnTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = Color.White,
    onSurface = Color.White
)

val LightRatingColorScheme = RatingColorScheme(
    unrated = Color(0xFF646464),
    low = Color(0xFFB71C1C),
    medium = Color(0xFFFB8C00),
    high = Color(0xFFFFD54F),
    veryHigh = Color(0xFF7CB342),
    top = Color(0xFF388E3C)
)

val DarkRatingColorScheme = RatingColorScheme(
    unrated = Color(0xFF646464),
    low = Color(0xFFB71C1C),
    medium = Color(0xFFFB8C00),
    high = Color(0xFFFFD54F),
    veryHigh = Color(0xFF7CB342),
    top = Color(0xFF388E3C)
)