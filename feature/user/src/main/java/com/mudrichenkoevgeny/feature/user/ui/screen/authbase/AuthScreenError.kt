package com.mudrichenkoevgeny.feature.user.ui.screen.authbase

import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.UserInputError

data class AuthScreenError(
    val userInputErrorList: List<UserInputError> = emptyList(),
    val resultError: Result.Failure? = null
)