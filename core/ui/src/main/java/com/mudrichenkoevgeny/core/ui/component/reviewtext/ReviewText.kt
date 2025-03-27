package com.mudrichenkoevgeny.core.ui.component.reviewtext

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mudrichenkoevgeny.core.common.config.DefaultConfig
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.R
import com.mudrichenkoevgeny.core.ui.component.circlebutton.CircleButton
import com.mudrichenkoevgeny.core.ui.component.expandabletext.ExpandableText
import com.mudrichenkoevgeny.core.ui.theme.Theme
import com.mudrichenkoevgeny.core.ui.theme.getRatingColor

const val EDITABLE_REVIEW_NOT_EXPANDED_TEXT_MAX_LINES = 2
const val NON_EDITABLE_REVIEW_NOT_EXPANDED_TEXT_MAX_LINES = 5

@Composable
fun EditableReviewText(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    maxRating: Int,
    rating: Int,
    reviewText: String? = null,
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit
) {
    ReviewText(
        reviewTextType = ReviewTextType.EditableReview,
        modifier = modifier,
        isLoading = isLoading,
        maxRating = maxRating,
        rating = rating,
        reviewText = reviewText,
        onDeleteClicked = onDeleteClicked,
        onEditClicked = onEditClicked
    )
}

@Composable
fun NonEditableReviewText(
    modifier: Modifier = Modifier,
    userName: String,
    userAvatarUrl: String?,
    maxRating: Int,
    rating: Int,
    reviewText: String? = null
) {
    ReviewText(
        reviewTextType = ReviewTextType.NonEditableReview,
        modifier = modifier,
        userName = userName,
        userAvatarUrl = userAvatarUrl,
        maxRating = maxRating,
        rating = rating,
        reviewText = reviewText
    )
}

@Composable
private fun ReviewText(
    reviewTextType: ReviewTextType,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    userName: String = "",
    userAvatarUrl: String? = null,
    maxRating: Int,
    rating: Int,
    reviewText: String? = null,
    onDeleteClicked: () -> Unit = { },
    onEditClicked: () -> Unit = { }
) {
    val isPreview = LocalInspectionMode.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.component_padding_default))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            when (reviewTextType) {
                is ReviewTextType.EditableReview -> {
                    CircleButton(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        isLoading = isLoading,
                        actionButtonIcon = R.drawable.ic_delete,
                        actionButtonContentDescription = R.string.cd_delete,
                        onClicked = onDeleteClicked
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.component_padding_default)))
                    CircleButton(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        isLoading = isLoading,
                        actionButtonIcon = R.drawable.ic_edit,
                        actionButtonContentDescription = R.string.cd_edit,
                        onClicked = onEditClicked
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.component_padding_default)))
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        maxLines = 1,
                        text = stringResource(R.string.review_text_your_review)
                    )
                }
                is ReviewTextType.NonEditableReview -> {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userAvatarUrl)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_avatar),
                        error = painterResource(R.drawable.ic_avatar),
                        contentDescription = stringResource(R.string.cd_user_avatar),
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.review_avatar_size))
                            .clip(CircleShape)
                            .border(
                                dimensionResource(R.dimen.review_circle_border_width_size),
                                MaterialTheme.colorScheme.onPrimary,
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.component_padding_default)))
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        maxLines = 1,
                        text = userName
                    )
                }
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.component_padding_default)))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.review_rating_indicator_size))
                    .align(Alignment.CenterVertically)
                    .background(
                        getRatingColor(
                            maxRating = maxRating,
                            rating = rating
                        ),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = rating.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        if (!isPreview) {
            ExpandableText(
                text = reviewText ?: "",
                collapsedMaxLine = if (reviewTextType is ReviewTextType.EditableReview) {
                    EDITABLE_REVIEW_NOT_EXPANDED_TEXT_MAX_LINES
                } else {
                    NON_EDITABLE_REVIEW_NOT_EXPANDED_TEXT_MAX_LINES
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditableReviewTextDefaultPreview() {
    Theme {
        EditableReviewText(
            isLoading = false,
            maxRating = DefaultConfig.DEFAULT_MAX_RATING,
            rating = 51,
            reviewText = MockConstants.MOCK_TEXT_LARGE,
            onDeleteClicked = { },
            onEditClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NonEditableReviewTextDefaultPreview() {
    Theme {
        NonEditableReviewText(
            userName = MockConstants.MOCK_USER_NAME,
            userAvatarUrl = null,
            maxRating = DefaultConfig.DEFAULT_MAX_RATING,
            rating = 99,
            reviewText = MockConstants.MOCK_TEXT_LARGE
        )
    }
}

sealed class ReviewTextType {
    object EditableReview : ReviewTextType()
    object NonEditableReview : ReviewTextType()
}