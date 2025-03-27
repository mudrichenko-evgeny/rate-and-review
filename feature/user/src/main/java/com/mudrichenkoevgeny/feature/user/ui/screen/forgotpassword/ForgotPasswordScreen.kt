package com.mudrichenkoevgeny.feature.user.ui.screen.forgotpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mudrichenkoevgeny.feature.user.R
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.convertToText
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.ui.component.errortext.ErrorTextWithCloseButton
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButton
import com.mudrichenkoevgeny.core.ui.component.loadingbutton.LoadingButtonState
import com.mudrichenkoevgeny.core.ui.theme.Theme
import com.mudrichenkoevgeny.feature.user.result.getIncorrectEmailError
import com.mudrichenkoevgeny.core.ui.R as UIResources

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit
) {
    val screenUiState by viewModel.uiState.collectAsState()

    ForgotPasswordUI(
        screenUiState = screenUiState,
        onEmailChanged =  { viewModel.onEmailChanged(it) },
        onContinueClicked = { viewModel.onContinueClicked() },
        onCloseResultErrorClicked = { viewModel.onCloseResultErrorClicked() }
    )

    LaunchedEffect(Unit) {
        viewModel.dismissRequest.collect { onDismissRequest() }
    }
}

@Composable
private fun ForgotPasswordUI(
    screenUiState: ForgotPasswordUiState,
    onEmailChanged: (String) -> Unit,
    onContinueClicked: () -> Unit,
    onCloseResultErrorClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(UIResources.dimen.padding_base)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.auth_forgot_password),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_large)))
        OutlinedTextField(
            value = screenUiState.email,
            onValueChange = { onEmailChanged(it) },
            label = { Text(stringResource(R.string.auth_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            supportingText = {
                screenUiState.error?.userInputErrorList?.getIncorrectEmailError()?.let { incorrectEmailError ->
                    Text(
                        text = stringResource(R.string.auth_error_incorrect_email),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            enabled = !screenUiState.isSuccessResult,
            modifier = Modifier.fillMaxWidth()
        )
        if (screenUiState.isSuccessResult) {
            Spacer(modifier = Modifier.height(dimensionResource(UIResources.dimen.spacer_large)))
            Text(
                text = stringResource(R.string.auth_forgot_password_success_result_description),
                style = MaterialTheme.typography.bodyMedium
            )
        }
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
            text = stringResource(
                if (screenUiState.isSuccessResult) {
                    R.string.auth_forgot_password_close
                } else {
                    R.string.auth_forgot_password_send_to_email
                }
            ),
            state = when {
                screenUiState.isLoading -> LoadingButtonState.LOADING
                else -> LoadingButtonState.ENABLED
            },
            onClick = {
                onContinueClicked()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenDefaultPreview() {
    Theme {
        ForgotPasswordUI(
            screenUiState = ForgotPasswordUiState(
                email = MockConstants.MOCK_EMAIL,
                isLoading = false,
                isSuccessResult = false,
                error = null,
                resultErrorVisible = false
            ),
            onEmailChanged = { },
            onContinueClicked = { },
            onCloseResultErrorClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenErrorPreview() {
    val email = MockConstants.MOCK_EMAIL
    Theme {
        ForgotPasswordUI(
            screenUiState = ForgotPasswordUiState(
                email = email,
                isLoading = false,
                isSuccessResult = false,
                error = AuthScreenError(
                    resultError = Result.Failure.UserBlocked(email)
                ),
                resultErrorVisible = true
            ),
            onEmailChanged = { },
            onContinueClicked = { },
            onCloseResultErrorClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenSuccessPreview() {
    Theme {
        ForgotPasswordUI(
            screenUiState = ForgotPasswordUiState(
                email = MockConstants.MOCK_EMAIL,
                isLoading = false,
                isSuccessResult = true,
                error = null,
                resultErrorVisible = false
            ),
            onEmailChanged = { },
            onContinueClicked = { },
            onCloseResultErrorClicked = { }
        )
    }
}