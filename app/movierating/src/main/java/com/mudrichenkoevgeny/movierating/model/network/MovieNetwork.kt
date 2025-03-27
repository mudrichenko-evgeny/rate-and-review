package com.mudrichenkoevgeny.movierating.model.network

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.core.model.network.ReviewNetwork
import com.mudrichenkoevgeny.core.model.network.TagNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MovieNetwork(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("averageRating") val averageRating: Int?,
    @SerialName("numberOfRatings") val numberOfRatings: Int,
    @SerialName("userReview") val userReview: ReviewNetwork?,
    @SerialName("tags") val tags: List<TagNetwork>,
    @SerialName("releaseYear") val releaseYear: Int,
    @SerialName("durationInMinutes") val durationInMinutes: Int,
    @SerialName("genres") val genres: List<GenreNetwork>
)