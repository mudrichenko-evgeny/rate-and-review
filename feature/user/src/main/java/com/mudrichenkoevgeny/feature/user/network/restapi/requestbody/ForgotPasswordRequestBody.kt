package com.mudrichenkoevgeny.feature.user.network.restapi.requestbody

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ForgotPasswordRequestBody(
    @SerialName("email") val email: String
)