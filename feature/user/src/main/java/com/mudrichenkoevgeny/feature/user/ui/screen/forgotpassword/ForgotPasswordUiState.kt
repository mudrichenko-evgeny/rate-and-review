package com.mudrichenkoevgeny.feature.user.ui.screen.forgotpassword

import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isSuccessResult: Boolean = false,
    val error: AuthScreenError? = null,
    val resultErrorVisible: Boolean = false
)