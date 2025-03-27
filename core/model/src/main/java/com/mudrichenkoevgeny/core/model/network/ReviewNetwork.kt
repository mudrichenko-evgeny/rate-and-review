package com.mudrichenkoevgeny.core.model.network

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ReviewNetwork(
    @SerialName("id") val id: String,
    @SerialName("reviewableItemId") val reviewableItemId: String,
    @SerialName("reviewer") val reviewer: ReviewerNetwork,
    @SerialName("rating") val rating: Int,
    @SerialName("reviewText") val reviewText: String?,
    @SerialName("createdAt") val createdAt: Long
)