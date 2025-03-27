package com.mudrichenkoevgeny.core.storage.datastore

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.core.common.enums.AppearanceMode
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AppPreferences(
    val appearanceMode: AppearanceMode = AppearanceMode.default()
) {

    companion object {
        const val APP_PREFERENCES_FILE = "app_preferences.pb"
    }
}