package com.mudrichenkoevgeny.core.model.mapper

import com.mudrichenkoevgeny.core.model.domain.Reviewer
import com.mudrichenkoevgeny.core.model.network.ReviewerNetwork

fun ReviewerNetwork.toReviewer(): Reviewer {
    return Reviewer(
        id = this.id,
        name = this.name,
        avatarUrl = this.avatarUrl,
        numberOfReviews = this.numberOfReviews,
        averageRating = this.averageRating
    )
}