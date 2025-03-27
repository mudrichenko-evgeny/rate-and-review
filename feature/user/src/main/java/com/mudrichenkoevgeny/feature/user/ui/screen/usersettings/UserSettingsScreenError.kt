package com.mudrichenkoevgeny.feature.user.ui.screen.usersettings

import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.UserInputError

data class UserSettingsScreenError(
    val userInputErrorList: List<UserInputError> = emptyList(),
    val resultError: Result.Failure? = null
)