package com.mudrichenkoevgeny.feature.user.ui.screen.login

import androidx.annotation.VisibleForTesting
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButton
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButtonState
import com.mudrichenkoevgeny.feature.user.R
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.convertToText
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.R as UIResources
import com.mudrichenkoevgeny.core.ui.component.errortext.ErrorTextWithCloseButton
import com.mudrichenkoevgeny.core.ui.theme.Theme
import com.mudrichenkoevgeny.feature.user.result.getIncorrectEmailError
import com.mudrichenkoevgeny.feature.user.result.getShortPasswordError

const val LOGIN_SCREEN_EMAIL_FIELD_TAG = "EmailField"
const val LOGIN_SCREEN_PASSWORD_FIELD_TAG = "PasswordField"
const val LOGIN_SCREEN_LOGIN_BUTTON_TAG = "LoginButton"

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val screenUiState by viewModel.uiState.collectAsState()

    LoginScreenUI(
        screenUiState = screenUiState,
        onEmailChanged = { viewModel.onEmailChanged(it) },
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        onLoginClicked = { viewModel.onLoginClicked() },
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToForgotPassword = onNavigateToForgotPassword,
        onCloseResultErrorClicked = { viewModel.onCloseResultErrorClicked() }
    )

    LaunchedEffect(Unit) {
        viewModel.dismissRequest.collect { onDismissRequest() }
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun LoginScreenUI(
    screenUiState: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onCloseResultErrorClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(UIResources.dimen.padding_base)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.auth_login),
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
                .testTag(LOGIN_SCREEN_EMAIL_FIELD_TAG)
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
                .testTag(LOGIN_SCREEN_PASSWORD_FIELD_TAG),
            visualTransformation = PasswordVisualTransformation(),
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
            text = stringResource(R.string.auth_login),
            state = when {
                screenUiState.isLoading -> LoadingButtonState.LOADING
                screenUiState.isSuccessfulLogin -> LoadingButtonState.SUCCESSFUL
                else -> LoadingButtonState.ENABLED
            },
            onClick = {
                onLoginClicked()
            },
            modifier = Modifier.fillMaxWidth()
                .testTag(LOGIN_SCREEN_LOGIN_BUTTON_TAG)
        )
        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
        TextButton(onClick = onNavigateToRegister) {
            Text(stringResource(R.string.auth_to_registration))
        }
        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_default)))
        TextButton(onClick = onNavigateToForgotPassword) {
            Text(stringResource(R.string.auth_forgot_password))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenDefaultPreview() {
    Theme {
        LoginScreenUI(
            screenUiState = LoginUiState(
                email = MockConstants.MOCK_EMAIL,
                password = "123456",
                isLoading = false,
                isSuccessfulLogin = false,
                error = null
            ),
            onEmailChanged = { },
            onPasswordChanged = { },
            onLoginClicked = { },
            onNavigateToRegister = { },
            onNavigateToForgotPassword = { },
            onCloseResultErrorClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenResultErrorPreview() {
    val email = MockConstants.MOCK_EMAIL
    Theme {
        LoginScreenUI(
            screenUiState = LoginUiState(
                email = email,
                password = "123456",
                isLoading = false,
                isSuccessfulLogin = false,
                error = AuthScreenError(
                    resultError = Result.Failure.UserBlocked(email)
                ),
                resultErrorVisible = true
            ),
            onEmailChanged = { },
            onPasswordChanged = { },
            onLoginClicked = { },
            onNavigateToRegister = { },
            onNavigateToForgotPassword = { },
            onCloseResultErrorClicked = { }
        )
    }
}