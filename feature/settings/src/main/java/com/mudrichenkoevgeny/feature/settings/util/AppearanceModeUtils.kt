package com.mudrichenkoevgeny.feature.settings.util

import androidx.annotation.StringRes
import com.mudrichenkoevgeny.core.common.enums.AppearanceMode
import com.mudrichenkoevgeny.feature.settings.R

@StringRes fun AppearanceMode.getName(): Int {
    return when (this) {
        AppearanceMode.DARK -> R.string.appearance_mode_dark
        AppearanceMode.LIGHT -> R.string.appearance_mode_light
        AppearanceMode.SYSTEM -> R.string.appearance_mode_system
    }
}