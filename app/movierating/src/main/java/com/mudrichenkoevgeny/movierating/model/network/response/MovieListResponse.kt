package com.mudrichenkoevgeny.movierating.model.network.response

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.movierating.model.network.MovieNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MovieListResponse(
    @SerialName("page") val page: Int,
    @SerialName("movies") val movies: List<MovieNetwork>
)