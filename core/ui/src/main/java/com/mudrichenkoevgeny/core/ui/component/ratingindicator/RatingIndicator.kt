package com.mudrichenkoevgeny.core.ui.component.ratingindicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.common.config.DefaultConfig
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.common.util.formatToNumberWithSpaces
import com.mudrichenkoevgeny.core.ui.theme.Theme
import com.mudrichenkoevgeny.core.ui.theme.getRatingColor

@Composable
fun RatingIndicator(
    modifier: Modifier = Modifier,
    numberOfRatings: Int,
    averageRating: Int?,
    userRating: Int?,
    maxRating: Int,
    maxNumberOfRatings: Int = Int.MAX_VALUE
) {
    val displayedAverageRating: String = if (averageRating == null) {
        stringResource(R.string.rating_indicator_nullable_rating)
    } else {
        "$averageRating"
    }

    val displayedUserRating: String = if (userRating == null) {
        stringResource(R.string.rating_indicator_nullable_rating)
    } else {
        "$userRating"
    }

    Row(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .width(IntrinsicSize.Max)
                .background(
                    getRatingColor(
                        maxRating = maxRating,
                        rating = averageRating
                    )
                )
                .padding(
                    horizontal = dimensionResource(R.dimen.component_padding_small),
                    vertical = dimensionResource(R.dimen.component_padding_extra_small)
                )
        ) {
            // Invisible text, which is used to set the correct width of the visible text
            Text(
                text = "$maxRating (${maxNumberOfRatings.formatToNumberWithSpaces()})",
                modifier = Modifier.alpha(0f)
            )
            // Visible text
            Text(
                text = "$displayedAverageRating (${numberOfRatings.formatToNumberWithSpaces()})",
            )
        }
        Box(
            modifier = Modifier
                .width(dimensionResource(R.dimen.separator_width))
                .fillMaxHeight()
                .background(
                    getRatingColor(
                        maxRating = maxRating,
                        rating = null,
                    )
                )
        )
        Box(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .background(
                    getRatingColor(
                        maxRating = maxRating,
                        rating = userRating
                    )
                )
                .padding(
                    horizontal = dimensionResource(R.dimen.component_padding_small),
                    vertical = dimensionResource(R.dimen.component_padding_extra_small)
                )
        ) {
            // Invisible text, which is used to set the correct width of the visible text
            Text(text = "$maxRating", modifier = Modifier.alpha(0f))
            // Visible text
            Text(
                text = displayedUserRating,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingIndicatorDefaultPreview() {
    Theme {
        RatingIndicator(
            numberOfRatings = 1355,
            averageRating = 78,
            userRating = 70,
            maxRating = DefaultConfig.DEFAULT_MAX_RATING
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingIndicatorWithoutUserRatingPreview() {
    Theme {
        RatingIndicator(
            numberOfRatings = 1355,
            averageRating = 78,
            userRating = null,
            maxRating = DefaultConfig.DEFAULT_MAX_RATING
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingIndicatorWithoutGlobalRatingPreview() {
    Theme {
        RatingIndicator(
            numberOfRatings = 0,
            averageRating = null,
            userRating = 100,
            maxRating = DefaultConfig.DEFAULT_MAX_RATING
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingIndicatorWithoutAnyRatingPreview() {
    Theme {
        RatingIndicator(
            numberOfRatings = 0,
            averageRating = null,
            userRating = null,
            maxRating = DefaultConfig.DEFAULT_MAX_RATING
        )
    }
}