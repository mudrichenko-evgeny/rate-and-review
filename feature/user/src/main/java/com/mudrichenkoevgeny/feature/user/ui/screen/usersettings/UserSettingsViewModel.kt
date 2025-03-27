package com.mudrichenkoevgeny.feature.user.ui.screen.usersettings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.core.storage.repository.files.FilesRepository
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.UserInputError
import com.mudrichenkoevgeny.feature.user.result.addError
import com.mudrichenkoevgeny.feature.user.util.checkAvatarFileSize
import com.mudrichenkoevgeny.feature.user.util.checkUserNameForErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val filesRepository: FilesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserSettingsUiState())
    val uiState: StateFlow<UserSettingsUiState> = _uiState

    private val _closeScreenRequest = MutableSharedFlow<Unit>()
    val closeScreenRequest: SharedFlow<Unit> = _closeScreenRequest

    init {
        viewModelScope.launch {
            userRepository.userDataFlow.collect { userData ->
                _uiState.update { it.copy(userData = userData) }
            }
        }
    }

    fun onUserNameChanged(userName: String) {
        _uiState.value = _uiState.value.copy(
            userName = userName,
            isSuccessfullySaved = false
        )
    }

    fun onAvatarPicked(avatarFileUri: Uri?) {
        viewModelScope.launch {
            val file = filesRepository.getFileFromUri(avatarFileUri)
            val userInputErrorList = _uiState.value.error?.userInputErrorList ?: emptyList()

            var hasImageError: Boolean = false
            if (file == null) {
                hasImageError = true
                userInputErrorList.addError(UserInputError.IncorrectImage.Unknown)
            } else {
                val fileSizeTooLargeError = checkAvatarFileSize(file)
                if (fileSizeTooLargeError != null) {
                    hasImageError = true
                    userInputErrorList.addError(fileSizeTooLargeError)
                }
            }

            if (hasImageError) {
                _uiState.value = _uiState.value.copy(
                    userAvatarFile = null,
                    isShouldDeleteAvatar = false,
                    error = UserSettingsScreenError(
                        userInputErrorList = userInputErrorList,
                        resultError = null
                    )
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    userAvatarFile = file,
                    isShouldDeleteAvatar = false
                )
            }
        }
    }

    fun onCancelAvatarChangesClicked() {
        _uiState.value = _uiState.value.copy(
            userAvatarFile = null,
            isShouldDeleteAvatar = false
        )
    }

    fun onDeleteAvatarClicked() {
        _uiState.value = _uiState.value.copy(
            userAvatarFile = null,
            isShouldDeleteAvatar = true
        )
    }

    fun onDeleteAccountClicked() {
        _uiState.value = _uiState.value.copy(
            isWaitingForDeleteAccountResult = true
        )
        viewModelScope.launch {
            val deleteAccountResult = userRepository.deleteAccount()
            when (deleteAccountResult) {
                is Result.Success<*> -> {
                    _closeScreenRequest.emit(Unit)
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isWaitingForDeleteAccountResult = false,
                        error = UserSettingsScreenError(
                            resultError = deleteAccountResult
                        ),
                        resultErrorVisible = true
                    )
                }
            }
        }
    }

    fun onSaveChangesClicked() {
        val userInputErrorList = _uiState.value.error?.userInputErrorList ?: emptyList()
        val userName = _uiState.value.userName
        val userAvatarFile = _uiState.value.userAvatarFile
        val isShouldDeleteAvatar = _uiState.value.isShouldDeleteAvatar
        val userNameError = checkUserNameForErrors(userName)
        if (userNameError != null) {
            userInputErrorList.addError(userNameError)
        }
        if (userInputErrorList.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                error = UserSettingsScreenError(
                    userInputErrorList = userInputErrorList
                )
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            isWaitingForSaveResult = true
        )
        viewModelScope.launch {
            val saveUserDataResult = userRepository.saveUserData(
                name = userName,
                isShouldDeleteAvatar = isShouldDeleteAvatar,
                avatarFile = if (isShouldDeleteAvatar) {
                    null
                } else {
                    userAvatarFile
                }
            )
            when (saveUserDataResult) {
                is Result.Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        isWaitingForSaveResult = false,
                        isSuccessfullySaved = true,
                        error = null,
                        resultErrorVisible = false
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isWaitingForSaveResult = false,
                        isSuccessfullySaved = false,
                        error = UserSettingsScreenError(
                            resultError = saveUserDataResult
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