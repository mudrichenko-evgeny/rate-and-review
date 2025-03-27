package com.mudrichenkoevgeny.feature.user.ui.screen.registration

import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError

data class RegistrationUiState(
    val email: String = "",
    val password: String = "",
    val userName: String = "",
    val isLoading: Boolean = false,
    val isSuccessfulRegistration: Boolean = false,
    val error: AuthScreenError? = null,
    val resultErrorVisible: Boolean = false
)