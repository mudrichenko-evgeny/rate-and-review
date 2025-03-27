package com.mudrichenkoevgeny.feature.user.ui.screen.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.UserInputError
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthBaseViewModel
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError
import com.mudrichenkoevgeny.feature.user.util.checkEmailForErrors
import com.mudrichenkoevgeny.feature.user.util.checkPasswordForErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : AuthBaseViewModel() {

    @Suppress("PropertyName")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    override fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email
        )
    }

    override fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password
        )
    }

    fun onLoginClicked() {
        val userInputErrorList: MutableList<UserInputError> = mutableListOf()
        val email = _uiState.value.email
        val password = _uiState.value.password
        val emailError = checkEmailForErrors(email)
        if (emailError != null) {
            userInputErrorList.add(UserInputError.IncorrectEmail)
        }
        val passwordError = checkPasswordForErrors(password)
        if (passwordError != null) {
            userInputErrorList.add(passwordError)
        }
        if (userInputErrorList.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isSuccessfulLogin = false,
                error = AuthScreenError(
                    userInputErrorList = userInputErrorList,
                    resultError = null
                ),
                resultErrorVisible = false
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            isSuccessfulLogin = false,
            error = null,
            resultErrorVisible = false
        )
        viewModelScope.launch {
            val loginResult = userRepository.login(email, password)
            when (loginResult) {
                is Result.Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccessfulLogin = true,
                        error = null,
                        resultErrorVisible = false
                    )
                    dismissAuthScreen()
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccessfulLogin = false,
                        error = AuthScreenError(
                            resultError = loginResult
                        ),
                        resultErrorVisible = true
                    )
                }
            }
        }
    }

    fun onCloseResultErrorClicked() {
        _uiState.value = _uiState.value.copy(
            resultErrorVisible = false
        )
    }
}