package com.mudrichenkoevgeny.feature.settings.ui.screen.aboutapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AboutAppViewModel @Inject constructor() : ViewModel() {

    private var _uiState = MutableStateFlow<AboutAppUiState>(AboutAppUiState.Loading)
    val uiState: StateFlow<AboutAppUiState> = _uiState

    fun onReceivedAppDetails(aboutAppArgs: AboutAppArgs) {
        _uiState.value = AboutAppUiState.Default(
            appDescription = aboutAppArgs.appDescription,
            userAgreementUrl = aboutAppArgs.userAgreementUrl
        )
    }
}