package com.mudrichenkoevgeny.movierating.model.network.response

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.core.model.network.ReviewNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ReviewListResponse(
    @SerialName("page") val page: Int,
    @SerialName("reviews") val reviews: List<ReviewNetwork>
)