package com.mudrichenkoevgeny.feature.user.datastore

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.feature.user.model.data.UserData
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserPreferences(
    val userData: UserData? = null
) {

    companion object {
        const val USER_PREFERENCES_FILE = "user_preferences.pb"
    }
}