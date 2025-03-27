package com.mudrichenkoevgeny.feature.user.ui.screen.login

import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccessfulLogin: Boolean = false,
    val error: AuthScreenError? = null,
    val resultErrorVisible: Boolean = false
)