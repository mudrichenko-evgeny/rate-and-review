package com.mudrichenkoevgeny.feature.user.ui.screen.login

import com.mudrichenkoevgeny.feature.user.model.data.UserData
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.feature.user.MainDispatcherRule
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.getIncorrectEmailError
import com.mudrichenkoevgeny.feature.user.result.getShortPasswordError
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTests {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel
    private val userRepository: UserRepository = mockk()

    @Before
    fun setup() {
        viewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `GIVEN email WHEN onEmailChanged() with email invoked THEN uiState will be with email`() =
        runTest {
            // GIVEN
            val email = "test@example.com"
            // WHEN
            viewModel.onEmailChanged(email)
            // THEN
            assertEquals(email, viewModel.uiState.value.email)
        }

    @Test
    fun `GIVEN password WHEN onPasswordChanged() with password invoked THEN uiState will be with password`() =
        runTest {
            // GIVEN
            val password = "password123"
            // WHEN
            viewModel.onPasswordChanged(password)
            // THEN
            assertEquals(password, viewModel.uiState.value.password)
        }

    @Test
    fun `GIVEN incorrect email WHEN onLoginClicked() invoked THEN uiState will be with IncorrectEmail error`() =
        runTest {
            // GIVEN
            val invalidEmail = "invalid email"
            val password = "password123"
            // WHEN
            viewModel.onEmailChanged(invalidEmail)
            viewModel.onPasswordChanged(password)
            viewModel.onLoginClicked()
            // THEN
            assertNotNull(viewModel.uiState.value.error?.userInputErrorList?.getIncorrectEmailError())
        }

    @Test
    fun `GIVEN incorrect password WHEN onLoginClicked() invoked THEN uiState will be with ShortPassword error`() =
        runTest {
            // GIVEN
            val email = "test@example.com"
            val invalidPassword = "123"
            // WHEN
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(invalidPassword)
            viewModel.onLoginClicked()
            // THEN
            assertNotNull(viewModel.uiState.value.error?.userInputErrorList?.getShortPasswordError())
        }

    @Test
    fun `GIVEN correct email and passwords WHEN onLoginClicked() invoked THEN uiState will be with successful login and without error`() =
        runTest {
            // GIVEN
            val email = "test@example.com"
            val password = "password123"
            coEvery {
                userRepository.login(
                    email,
                    password
                )
            } returns Result.Success<UserData>(mockk())
            // WHEN
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.onLoginClicked()
            // THEN
            assertTrue(viewModel.uiState.value.isLoading)
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.isSuccessfulLogin)
            assertNull(viewModel.uiState.value.error)
        }

    @Test
    fun `GIVEN correct email and passwords WHEN onLoginClicked() invoked and login returns IncorrectLoginOrPassword result THEN uiState will be with error`() =
        runTest {
            // GIVEN
            val email = "test@example.com"
            val password = "password123"
            coEvery {
                userRepository.login(
                    email,
                    password
                )
            } returns Result.Failure.IncorrectLoginOrPassword
            // WHEN
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.onLoginClicked()
            // THEN
            advanceUntilIdle()
            assertNotNull(viewModel.uiState.value.error?.resultError)
            assertTrue(viewModel.uiState.value.resultErrorVisible)
        }

    @Test
    fun `GIVEN uiState with visible resultError WHEN onCloseResultErrorClicked() invoked THEN uiState will be without visible resultError`() =
        runTest {
            // GIVEN
            viewModel._uiState.value = LoginUiState()
            viewModel.onLoginClicked()
            advanceUntilIdle()
            // WHEN
            viewModel.onCloseResultErrorClicked()
            // THEN
            assertFalse(viewModel.uiState.value.resultErrorVisible)
        }
}