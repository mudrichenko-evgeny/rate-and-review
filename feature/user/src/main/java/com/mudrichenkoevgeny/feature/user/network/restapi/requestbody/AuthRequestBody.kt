package com.mudrichenkoevgeny.feature.user.network.restapi.requestbody

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AuthRequestBody(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)