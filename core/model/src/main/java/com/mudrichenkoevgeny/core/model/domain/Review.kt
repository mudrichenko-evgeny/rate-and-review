package com.mudrichenkoevgeny.core.model.domain

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Review(
    val id: String,
    val reviewableItemId: String,
    val reviewer: Reviewer,
    val rating: Int,
    val reviewText: String?,
    val createdAt: Long
)

fun getMockReview(
    reviewer: Reviewer = getMockReviewer()
) = Review(
    id = "1",
    reviewableItemId = "1",
    reviewer = reviewer,
    rating = 75,
    reviewText = MockConstants.MOCK_TEXT_LARGE,
    createdAt = 1_739_274_752_263_000
)