package com.mudrichenkoevgeny.movierating.network.requestbody

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class SaveReviewRequestBody(
    @SerialName("movieId") val movieId: String,
    @SerialName("rating") val rating: Int,
    @SerialName("reviewText") val reviewText: String,
)