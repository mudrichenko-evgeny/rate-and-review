package com.mudrichenkoevgeny.feature.user.ui.screen.usersettings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.component.circlebutton.CircleButton
import com.mudrichenkoevgeny.core.ui.component.errortext.ErrorText
import com.mudrichenkoevgeny.core.ui.component.errortext.ErrorTextWithCloseButton
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButton
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButtonState
import com.mudrichenkoevgeny.core.ui.dialog.alert.ConfirmationDialog
import com.mudrichenkoevgeny.core.ui.theme.Theme
import com.mudrichenkoevgeny.feature.user.R
import com.mudrichenkoevgeny.feature.user.model.data.getMockUserData
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.UserInputError
import com.mudrichenkoevgeny.feature.user.result.convertToText
import com.mudrichenkoevgeny.feature.user.result.getIncorrectImageError
import com.mudrichenkoevgeny.feature.user.result.getShortUserNameError
import com.mudrichenkoevgeny.core.ui.R as UIResources

@Composable
fun UserSettingsScreen(
    viewModel: UserSettingsViewModel = hiltViewModel(),
    onCloseScreen: () -> Unit
) {
    val screenUiState by viewModel.uiState.collectAsState()

    val showDeleteAccountConfirmationDialog = remember { mutableStateOf(false) }
    if (showDeleteAccountConfirmationDialog.value) {
        ConfirmationDialog(
            title = null,
            text = stringResource(R.string.user_settings_delete_account_confirmation_text),
            confirmButtonText = stringResource(R.string.user_settings_delete_account_confirmation_yes),
            dismissButtonText = stringResource(R.string.user_settings_delete_account_confirmation_no),
            onDismiss = { showDeleteAccountConfirmationDialog.value = false },
            onConfirm = {
                viewModel.onDeleteAccountClicked()
                showDeleteAccountConfirmationDialog.value = false
            }
        )
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
        viewModel.onAvatarPicked(uri)
    }

    UserSettingsScreenUI(
        screenUiState = screenUiState,
        onPickAvatarClicked = { launcher.launch("image/*") },
        onUserNameChanged = { viewModel.onUserNameChanged(it) },
        onCancelAvatarChangesClicked = { viewModel.onCancelAvatarChangesClicked() },
        onDeleteAvatarClicked = { viewModel.onDeleteAvatarClicked() },
        onDeleteAccountClicked = { showDeleteAccountConfirmationDialog.value = true },
        onSaveClicked = { viewModel.onSaveChangesClicked() },
        onCloseResultErrorClicked = { viewModel.onCloseResultErrorClicked() }
    )

    LaunchedEffect(Unit) {
        viewModel.closeScreenRequest.collect { onCloseScreen() }
    }
}

@Composable
private fun UserSettingsScreenUI(
    screenUiState: UserSettingsUiState,
    onPickAvatarClicked: () -> Unit,
    onCancelAvatarChangesClicked: () -> Unit,
    onDeleteAvatarClicked: () -> Unit,
    onUserNameChanged: (String) -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onCloseResultErrorClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(UIResources.dimen.component_padding_large))
    ) {
        // Top
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                val imageModel = when {
                    screenUiState.isShouldDeleteAvatar -> null
                    screenUiState.userAvatarFile != null -> {
                        screenUiState.userAvatarFile
                    }
                    else -> {
                        ImageRequest.Builder(LocalContext.current)
                            .data(screenUiState.userData?.avatarUrl)
                            .build()
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(0.55f)
                        .padding(horizontal = dimensionResource(UIResources.dimen.spacer_large))
                ) {
                    AsyncImage(
                        model = imageModel,
                        placeholder = painterResource(UIResources.drawable.ic_avatar),
                        error = painterResource(UIResources.drawable.ic_avatar),
                        contentDescription = stringResource(R.string.cd_profile_user_avatar),
                        modifier = Modifier
                            .size(dimensionResource(UIResources.dimen.profile_avatar_size))
                            .clip(CircleShape)
                            .border(
                                dimensionResource(UIResources.dimen.profile_avatar_border_width),
                                MaterialTheme.colorScheme.onPrimary,
                                CircleShape
                            )
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(0.15f)
                ) {
                    CircleButton(
                        size = dimensionResource(UIResources.dimen.action_button_image_size_large),
                        isEnabled = screenUiState.isCancelAvatarChangesButtonEnabled(),
                        actionButtonIcon = UIResources.drawable.ic_cancel,
                        actionButtonContentDescription = UIResources.string.cd_cancel,
                        onClicked = { onCancelAvatarChangesClicked() }
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(UIResources.dimen.spacer_large)))
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(0.15f)
                ) {
                    CircleButton(
                        size = dimensionResource(UIResources.dimen.action_button_image_size_large),
                        isEnabled = screenUiState.isDeleteAvatarButtonEnabled(),
                        actionButtonIcon = UIResources.drawable.ic_delete,
                        actionButtonContentDescription = UIResources.string.cd_delete,
                        onClicked = { onDeleteAvatarClicked() }
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(UIResources.dimen.spacer_large)))
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(0.15f)
                ) {
                    CircleButton(
                        size = dimensionResource(UIResources.dimen.action_button_image_size_large),
                        isEnabled = screenUiState.isPickAvatarButtonEnabled(),
                        actionButtonIcon = UIResources.drawable.ic_edit,
                        actionButtonContentDescription = UIResources.string.cd_edit,
                        onClicked = { onPickAvatarClicked() }
                    )
                }
            }
            screenUiState.error?.userInputErrorList?.getIncorrectImageError()?.let { incorrectImageFailure ->
                Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
                ErrorText(
                    text = when (incorrectImageFailure) {
                        is UserInputError.IncorrectImage.FileSizeTooLarge -> {
                            stringResource(
                                R.string.user_settings_incorrect_image_error_file_size,
                                incorrectImageFailure.maxSizeMb,
                                incorrectImageFailure.currentSizeMb
                            )
                        }
                        is UserInputError.IncorrectImage.Unknown -> {
                            stringResource(R.string.user_settings_incorrect_image_error_unknown)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.component_padding_default)))
            OutlinedTextField(
                value = screenUiState.userName,
                onValueChange = { onUserNameChanged(it)},
                label = { Text(stringResource(R.string.auth_user_name)) },
                supportingText = {
                    screenUiState.error?.userInputErrorList?.getShortUserNameError()?.let { shortUserNameFailure ->
                        Text(
                            text = stringResource(
                                R.string.auth_error_short_user_name,
                                shortUserNameFailure.minimumLength,
                                shortUserNameFailure.currentLength
                            ),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        // Bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            screenUiState.error?.resultError?.let { resultError ->
                if (screenUiState.resultErrorVisible) {
                    ErrorTextWithCloseButton(
                        text = resultError.convertToText(),
                        onCloseButtonClicked = {
                            onCloseResultErrorClicked()
                        }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
                }
            }
            LoadingButton(
                text = stringResource(R.string.user_settings_delete_account),
                state = when {
                    screenUiState.isWaitingForDeleteAccountResult -> LoadingButtonState.LOADING
                    screenUiState.isDeleteAccountButtonEnabled() -> LoadingButtonState.ENABLED
                    else -> LoadingButtonState.DISABLED
                },
                onClick = {
                    onDeleteAccountClicked()
                },
                modifier = Modifier.fillMaxWidth()
            )
            LoadingButton(
                text = stringResource(R.string.user_settings_save_changes),
                state = when {
                    screenUiState.isWaitingForSaveResult -> LoadingButtonState.LOADING
                    screenUiState.isSuccessfullySaved -> LoadingButtonState.SUCCESSFUL
                    screenUiState.isSaveButtonEnabled() -> LoadingButtonState.ENABLED
                    else -> LoadingButtonState.DISABLED
                },
                onClick = {
                    onSaveClicked()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserSettingsScreenDefaultPreview() {
    Theme {
        UserSettingsScreenUI(
            screenUiState = UserSettingsUiState(
                userData = getMockUserData(),
                userName = MockConstants.MOCK_USER_NAME
            ),
            onPickAvatarClicked = { },
            onUserNameChanged = { },
            onCancelAvatarChangesClicked = { },
            onDeleteAvatarClicked = { },
            onDeleteAccountClicked = { },
            onSaveClicked = { },
            onCloseResultErrorClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserSettingsScreenWithErrorsPreview() {
    Theme {
        UserSettingsScreenUI(
            screenUiState = UserSettingsUiState(
                userData = getMockUserData(),
                error = UserSettingsScreenError(
                    userInputErrorList = listOf(
                        UserInputError.IncorrectImage.Unknown,
                        UserInputError.ShortUserName(5, 0)
                    ),
                    resultError = Result.Failure.Unknown
                ),
                resultErrorVisible = true
            ),
            onPickAvatarClicked = { },
            onUserNameChanged = { },
            onCancelAvatarChangesClicked = { },
            onDeleteAvatarClicked = { },
            onDeleteAccountClicked = { },
            onSaveClicked = { },
            onCloseResultErrorClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserSettingsScreenWaitingForSaveResultPreview() {
    Theme {
        UserSettingsScreenUI(
            screenUiState = UserSettingsUiState(
                userData = getMockUserData(),
                userName = MockConstants.MOCK_USER_NAME,
                isWaitingForSaveResult = true
            ),
            onPickAvatarClicked = { },
            onUserNameChanged = { },
            onCancelAvatarChangesClicked = { },
            onDeleteAvatarClicked = { },
            onDeleteAccountClicked = { },
            onSaveClicked = { },
            onCloseResultErrorClicked = { }
        )
    }
}