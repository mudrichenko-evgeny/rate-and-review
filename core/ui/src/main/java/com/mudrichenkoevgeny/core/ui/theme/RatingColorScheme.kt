package com.mudrichenkoevgeny.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class RatingColorScheme(
    val unrated: Color,
    val low: Color,
    val medium: Color,
    val high: Color,
    val veryHigh: Color,
    val top: Color
)

val LocalRatingColorScheme = staticCompositionLocalOf {
    LightRatingColorScheme
}

@Composable
fun getRatingColor(
    maxRating: Int,
    rating: Int?
): Color {
    val ratingColors = LocalRatingColorScheme.current
    rating ?: return ratingColors.unrated

    val ratingPercent = (rating.toFloat() / maxRating) * 100

    return remember(rating) {
        when (ratingPercent) {
            in 0f..40f -> ratingColors.low
            in 41f..59f -> ratingColors.medium
            in 60f..69f -> ratingColors.high
            in 70f..84f -> ratingColors.veryHigh
            else -> ratingColors.top
        }
    }
}

val LightRatingColorScheme = RatingColorScheme(
    unrated = Color(0xFF646464),
    low = Color(0xFFB71C1C),
    medium = Color(0xFFFB8C00),
    high = Color(0xFFFFD54F),
    veryHigh = Color(0xFF7CB342),
    top = Color(0xFF388E3C)
)