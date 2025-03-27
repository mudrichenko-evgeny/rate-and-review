package com.mudrichenkoevgeny.core.model.network

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ReviewerNetwork(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("avatarUrl") val avatarUrl: String?,
    @SerialName("numberOfReviews") val numberOfReviews: Int,
    @SerialName("averageRating") val averageRating: Int?
)