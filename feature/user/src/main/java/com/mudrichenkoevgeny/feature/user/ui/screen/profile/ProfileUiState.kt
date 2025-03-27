package com.mudrichenkoevgeny.feature.user.ui.screen.profile

import com.mudrichenkoevgeny.feature.user.model.data.UserData

data class ProfileUiState(
    val userData: UserData? = null
) {
    fun isUserAuthorized(): Boolean = userData != null
}