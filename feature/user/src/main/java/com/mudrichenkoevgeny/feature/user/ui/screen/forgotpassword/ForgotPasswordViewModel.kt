package com.mudrichenkoevgeny.feature.user.ui.screen.forgotpassword

import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthBaseViewModel
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError
import com.mudrichenkoevgeny.feature.user.util.checkEmailForErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : AuthBaseViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    override fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email
        )
    }

    override fun onPasswordChanged(password: String) { }

    fun onContinueClicked() {
        if (_uiState.value.isSuccessResult) {
            onCloseClicked()
        } else {
            sendForgotPasswordRequest()
        }
    }

    fun onCloseResultErrorClicked() {
        _uiState.value = _uiState.value.copy(
            resultErrorVisible = false
        )
    }

    private fun sendForgotPasswordRequest() {
        val email = _uiState.value.email
        val emailError = checkEmailForErrors(email)
        if (emailError != null) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isSuccessResult = false,
                error = AuthScreenError(
                    userInputErrorList = listOf(emailError)
                ),
                resultErrorVisible = false
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            isSuccessResult = false,
            error = null,
            resultErrorVisible = false
        )
        viewModelScope.launch {
            val forgotPasswordResult = userRepository.forgotPassword(email)
            when (forgotPasswordResult) {
                is Result.Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccessResult = true,
                        error = null,
                        resultErrorVisible = false
                    )
                    dismissAuthScreen()
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccessResult = false,
                        error = AuthScreenError(
                            resultError = forgotPasswordResult
                        ),
                        resultErrorVisible = true
                    )
                }
            }
        }
    }

    fun onCloseClicked() {
        dismissAuthScreenNow()
    }
}