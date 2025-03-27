package com.mudrichenkoevgeny.core.model.network

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TagNetwork(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String
)