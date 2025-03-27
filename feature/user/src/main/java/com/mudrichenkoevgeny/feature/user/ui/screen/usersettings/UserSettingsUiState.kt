package com.mudrichenkoevgeny.feature.user.ui.screen.usersettings

import com.mudrichenkoevgeny.feature.user.model.data.UserData
import java.io.File

data class UserSettingsUiState(
    val userData: UserData? = null,
    val userName: String = "",
    val userAvatarFile: File? = null,
    val isShouldDeleteAvatar: Boolean = false,
    val isWaitingForDeleteAccountResult: Boolean = false,
    val isWaitingForSaveResult: Boolean = false,
    val isSuccessfullySaved: Boolean = false,
    val error: UserSettingsScreenError? = null,
    val resultErrorVisible: Boolean = false
) {

    fun isCancelAvatarChangesButtonEnabled(): Boolean = isChangeAvatarButtonsEnabled()

    fun isDeleteAvatarButtonEnabled(): Boolean = isChangeAvatarButtonsEnabled()
            && (userData?.avatarUrl != null || userAvatarFile != null)
            && !isShouldDeleteAvatar

    fun isPickAvatarButtonEnabled(): Boolean = isChangeAvatarButtonsEnabled()

    private fun isChangeAvatarButtonsEnabled(): Boolean = !isWaitingForSaveResult &&
            !isWaitingForDeleteAccountResult

    fun isDeleteAccountButtonEnabled(): Boolean = !isWaitingForSaveResult &&
            !isWaitingForDeleteAccountResult

    fun isSaveButtonEnabled(): Boolean = !isWaitingForSaveResult &&
            !isWaitingForDeleteAccountResult &&
            (isUserNameChanged() || userAvatarFile != null)

    private fun isUserNameChanged(): Boolean = userData?.name != userName
}