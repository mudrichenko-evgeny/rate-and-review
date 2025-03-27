package com.mudrichenkoevgeny.feature.user.model.network.response

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.feature.user.model.network.UserDataNetwork
import com.mudrichenkoevgeny.core.network.model.SessionTokenNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AuthResponse(
    @SerialName("user") val userData: UserDataNetwork,
    @SerialName("token") val sessionToken: SessionTokenNetwork
)