package com.mudrichenkoevgeny.feature.settings.ui.screen.aboutapp

sealed class AboutAppUiState {

    object Loading : AboutAppUiState()

    data class Default(
        val appDescription: String,
        val userAgreementUrl: String? = ""
    ) : AboutAppUiState() {

        fun hasUserAgreement(): Boolean {
            return !userAgreementUrl.isNullOrBlank()
        }
    }
}