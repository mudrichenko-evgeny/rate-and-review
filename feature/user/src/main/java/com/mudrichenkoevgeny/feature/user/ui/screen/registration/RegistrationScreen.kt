package com.mudrichenkoevgeny.feature.user.ui.screen.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mudrichenkoevgeny.feature.user.R
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.convertToText
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.R as UIResources
import com.mudrichenkoevgeny.core.ui.component.errortext.ErrorTextWithCloseButton
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButton
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButtonState
import com.mudrichenkoevgeny.core.ui.theme.Theme
import com.mudrichenkoevgeny.feature.user.result.getIncorrectEmailError
import com.mudrichenkoevgeny.feature.user.result.getShortPasswordError
import com.mudrichenkoevgeny.feature.user.result.getShortUserNameError

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val screenUiState by viewModel.uiState.collectAsState()

    RegistrationScreenUI(
        screenUiState = screenUiState,
        onEmailChanged = { viewModel.onEmailChanged(it) },
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        onUserNameChanged = { viewModel.onUserNameChanged(it) },
        onRegistrationClicked = { viewModel.onRegistrationClicked() },
        onNavigateToLogin = onNavigateToLogin,
        onCloseResultErrorClicked = { viewModel.onCloseResultErrorClicked() }
    )

    LaunchedEffect(Unit) {
        viewModel.dismissRequest.collect { onDismissRequest() }
    }
}

@Composable
private fun RegistrationScreenUI(
    screenUiState: RegistrationUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onUserNameChanged: (String) -> Unit,
    onRegistrationClicked: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onCloseResultErrorClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(UIResources.dimen.padding_base)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.auth_register),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_large)))
        OutlinedTextField(
            value = screenUiState.email,
            onValueChange = { onEmailChanged(it) },
            label = { Text(stringResource(R.string.auth_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            supportingText = {
                screenUiState.error?.userInputErrorList?.getIncorrectEmailError()?.let {
                    Text(
                        text = stringResource(R.string.auth_error_incorrect_email),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
        OutlinedTextField(
            value = screenUiState.password,
            onValueChange = { onPasswordChanged(it)},
            label = { Text(stringResource(R.string.auth_password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            supportingText = {
                screenUiState.error?.userInputErrorList?.getShortPasswordError()?.let { shortPasswordError ->
                    Text(
                        text = stringResource(
                            R.string.auth_error_short_password,
                            shortPasswordError.minimumLength,
                            shortPasswordError.currentLength
                        ),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
        OutlinedTextField(
            value = screenUiState.userName,
            onValueChange = { onUserNameChanged(it)},
            label = { Text(stringResource(R.string.auth_user_name)) },
            supportingText = {
                screenUiState.error?.userInputErrorList?.getShortUserNameError()?.let { shortUserNameError ->
                    Text(
                        text = stringResource(
                            R.string.auth_error_short_user_name,
                            shortUserNameError.minimumLength,
                            shortUserNameError.currentLength
                        ),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        screenUiState.error?.resultError?.let { resultError ->
            if (screenUiState.resultErrorVisible) {
                Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
                ErrorTextWithCloseButton(
                    text = resultError.convertToText(),
                    onCloseButtonClicked = {
                        onCloseResultErrorClicked()
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_large)))
        LoadingButton(
            text = stringResource(R.string.auth_register),
            state = when {
                screenUiState.isLoading -> LoadingButtonState.LOADING
                screenUiState.isSuccessfulRegistration -> LoadingButtonState.SUCCESSFUL
                else -> LoadingButtonState.ENABLED
            },
            onClick = {
                onRegistrationClicked()
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
        TextButton(onClick = onNavigateToLogin) {
            Text(stringResource(R.string.auth_to_login))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationScreenDefaultPreview() {
    Theme {
        RegistrationScreenUI(
            screenUiState = RegistrationUiState(
                email = MockConstants.MOCK_EMAIL,
                password = "123456",
                userName = MockConstants.MOCK_USER_NAME,
                isLoading = false,
                isSuccessfulRegistration = false,
                error = null
            ),
            onEmailChanged = { },
            onPasswordChanged = { },
            onUserNameChanged = { },
            onRegistrationClicked = { },
            onNavigateToLogin = { },
            onCloseResultErrorClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationScreenResultErrorPreview() {
    val email = MockConstants.MOCK_EMAIL
    Theme {
        RegistrationScreenUI(
            screenUiState = RegistrationUiState(
                email = email,
                password = "123456",
                userName = MockConstants.MOCK_USER_NAME,
                isLoading = false,
                isSuccessfulRegistration = false,
                error = AuthScreenError(
                    resultError = Result.Failure.UserBlocked(email)
                ),
                resultErrorVisible = true
            ),
            onEmailChanged = { },
            onPasswordChanged = { },
            onUserNameChanged = { },
            onRegistrationClicked = { },
            onNavigateToLogin = { },
            onCloseResultErrorClicked = { }
        )
    }
}