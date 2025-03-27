package com.mudrichenkoevgeny.core.ui.component.reviewcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.common.config.DefaultConfig
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.component.basecard.BaseCard
import com.mudrichenkoevgeny.core.ui.component.reviewtext.NonEditableReviewText
import com.mudrichenkoevgeny.core.ui.theme.Theme

@Composable
fun ReviewCard(
    userName: String,
    userAvatarUrl: String?,
    maxRating: Int,
    rating: Int,
    reviewText: String?
) {
    BaseCard {
        NonEditableReviewText(
            userName = userName,
            userAvatarUrl = userAvatarUrl,
            maxRating = maxRating,
            rating = rating,
            reviewText = reviewText
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCardDefaultPreview() {
    Theme {
        ReviewCard(
            userName = MockConstants.MOCK_USER_NAME,
            userAvatarUrl = null,
            maxRating = DefaultConfig.DEFAULT_MAX_RATING,
            rating = 75,
            reviewText = MockConstants.MOCK_TEXT_LARGE
        )
    }
}