package com.mudrichenkoevgeny.core.network.model

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.core.network.constants.NetworkConstants.ACCESS_TOKEN_JSON_FIELD_NAME
import com.mudrichenkoevgeny.core.network.constants.NetworkConstants.REFRESH_TOKEN_JSON_FIELD_NAME
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class SessionTokenNetwork(
    @SerialName(ACCESS_TOKEN_JSON_FIELD_NAME) val accessToken: String,
    @SerialName(REFRESH_TOKEN_JSON_FIELD_NAME) val refreshToken: String
)