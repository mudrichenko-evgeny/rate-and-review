package com.mudrichenkoevgeny.feature.user.ui.screen.registration

import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.UserInputError
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthBaseViewModel
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError
import com.mudrichenkoevgeny.feature.user.util.checkEmailForErrors
import com.mudrichenkoevgeny.feature.user.util.checkPasswordForErrors
import com.mudrichenkoevgeny.feature.user.util.checkUserNameForErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : AuthBaseViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState

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

    fun onUserNameChanged(userName: String) {
        _uiState.value = _uiState.value.copy(
            userName = userName
        )
    }

    fun onRegistrationClicked() {
        val userInputErrorList: MutableList<UserInputError> = mutableListOf()
        val email = _uiState.value.email
        val password = _uiState.value.password
        val userName = _uiState.value.userName
        val emailError = checkEmailForErrors(email)
        if (emailError != null) {
            userInputErrorList.add(UserInputError.IncorrectEmail)
        }
        val passwordError = checkPasswordForErrors(password)
        if (passwordError != null) {
            userInputErrorList.add(passwordError)
        }
        val userNameError = checkUserNameForErrors(userName)
        if (userNameError != null) {
            userInputErrorList.add(userNameError)
        }
        if (userInputErrorList.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isSuccessfulRegistration = false,
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
            isSuccessfulRegistration = false,
            error = null,
            resultErrorVisible = false
        )
        viewModelScope.launch {
            val registrationResult = userRepository.register(email, password, userName)
            when (registrationResult) {
                is Result.Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccessfulRegistration = true,
                        error = null,
                        resultErrorVisible = false
                    )
                    dismissAuthScreen()
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccessfulRegistration = false,
                        error = AuthScreenError(
                            resultError = registrationResult
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