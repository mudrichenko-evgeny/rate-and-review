package com.mudrichenkoevgeny.movierating.model.network

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GenreNetwork(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String
)