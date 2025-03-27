package com.mudrichenkoevgeny.feature.user.ui.screen.login

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.mudrichenkoevgeny.feature.user.R
import com.mudrichenkoevgeny.feature.user.result.UserInputError
import com.mudrichenkoevgeny.feature.user.ui.screen.authbase.AuthScreenError
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenTests {

    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setupTests() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testLoginScreenComponents() {
        composeTestRule.setContent {
            LoginScreenUI(
                screenUiState = LoginUiState(),
                onEmailChanged = {},
                onPasswordChanged = {},
                onLoginClicked = {},
                onNavigateToRegister = {},
                onNavigateToForgotPassword = {},
                onCloseResultErrorClicked = {}
            )
        }

        composeTestRule.onNodeWithTag(LOGIN_SCREEN_EMAIL_FIELD_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOGIN_SCREEN_PASSWORD_FIELD_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOGIN_SCREEN_LOGIN_BUTTON_TAG).assertIsDisplayed()
    }

    @Test
    fun testLoginScreenEnteringEmailAndPassword() {
        var email = ""
        var password = ""
        composeTestRule.setContent {
            LoginScreenUI(
                screenUiState = LoginUiState(),
                onEmailChanged = { email = it },
                onPasswordChanged = { password = it },
                onLoginClicked = {},
                onNavigateToRegister = {},
                onNavigateToForgotPassword = {},
                onCloseResultErrorClicked = {}
            )
        }

        val testEmail = "test@example.com"
        val emailTextField = composeTestRule.onNodeWithTag(LOGIN_SCREEN_EMAIL_FIELD_TAG)
        emailTextField.performTextInput(testEmail)
        assertEquals(testEmail, email)

        val testPassword = "password123"
        val passwordTextField = composeTestRule.onNodeWithTag(LOGIN_SCREEN_PASSWORD_FIELD_TAG)
        passwordTextField.performTextInput(testPassword)
        assertEquals(testPassword, password)
    }

    @Test
    fun testLoginScreenErrorMessageIsDisplayed() {
        composeTestRule.setContent {
            LoginScreenUI(
                screenUiState = LoginUiState(
                    error = AuthScreenError(
                        userInputErrorList = listOf(UserInputError.IncorrectEmail)
                    )
                ),
                onEmailChanged = {},
                onPasswordChanged = {},
                onLoginClicked = {},
                onNavigateToRegister = {},
                onNavigateToForgotPassword = {},
                onCloseResultErrorClicked = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.auth_error_incorrect_email))
            .assertIsDisplayed()
    }

    @Test
    fun testLoginScreenLoginButtonIsDisabledWhenLoading() {
        composeTestRule.setContent {
            LoginScreenUI(
                screenUiState = LoginUiState(isLoading = true),
                onEmailChanged = {},
                onPasswordChanged = {},
                onLoginClicked = {},
                onNavigateToRegister = {},
                onNavigateToForgotPassword = {},
                onCloseResultErrorClicked = {}
            )
        }

        composeTestRule.onNodeWithTag(LOGIN_SCREEN_LOGIN_BUTTON_TAG).assertIsNotEnabled()
    }
}