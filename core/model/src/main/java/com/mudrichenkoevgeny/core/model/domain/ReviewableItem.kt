package com.mudrichenkoevgeny.core.model.domain

abstract class ReviewableItem(
    open val id: String,
    open val name: String,
    open val imageUrl: String?,
    open val averageRating: Int?,
    open val numberOfRatings: Int,
    open val userReview: Review?,
    open val tags: List<Tag>
)