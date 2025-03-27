package com.mudrichenkoevgeny.feature.user.ui.screen.authbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

const val DISMISS_DIALOG_DELAY_MS: Long = 2000

abstract class AuthBaseViewModel : ViewModel() {

    private val _dismissRequest = MutableSharedFlow<Unit>()
    val dismissRequest: SharedFlow<Unit> = _dismissRequest

    abstract fun onEmailChanged(email: String)

    abstract fun onPasswordChanged(password: String)

    internal fun dismissAuthScreen() {
        dismissAuthScreen(true)
    }

    internal fun dismissAuthScreenNow() {
        dismissAuthScreen(false)
    }

    private fun dismissAuthScreen(withDelay: Boolean) {
        viewModelScope.launch{
            if (withDelay) {
                delay(DISMISS_DIALOG_DELAY_MS)
            }
            _dismissRequest.emit(Unit)
        }
    }
}