package com.mudrichenkoevgeny.core.model.mapper

import com.mudrichenkoevgeny.core.model.domain.Review
import com.mudrichenkoevgeny.core.model.network.ReviewNetwork

fun ReviewNetwork.toReview(): Review {
    return Review(
        id = this.id,
        reviewableItemId = this.reviewableItemId,
        reviewer = this.reviewer.toReviewer(),
        rating = this.rating,
        reviewText = this.reviewText,
        createdAt = this.createdAt
    )
}