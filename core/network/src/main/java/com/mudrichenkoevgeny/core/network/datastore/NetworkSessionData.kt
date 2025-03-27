package com.mudrichenkoevgeny.core.network.datastore

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NetworkSessionData(
    val accessToken: String? = null,
    val refreshToken: String? = null
) {

    companion object {
        const val NETWORK_SESSION_PREFERENCES_FILE = "network_session_preferences.pb"
    }
}