package com.mudrichenkoevgeny.feature.user.repository

import androidx.datastore.core.DataStore
import com.mudrichenkoevgeny.feature.user.datastore.UserPreferences
import com.mudrichenkoevgeny.feature.user.model.mapper.toUserData
import com.mudrichenkoevgeny.feature.user.model.network.response.AuthResponse
import com.mudrichenkoevgeny.feature.user.model.network.UserDataNetwork
import com.mudrichenkoevgeny.feature.user.network.restapi.auth.AuthRestApi
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.core.network.model.SessionTokenNetwork
import com.mudrichenkoevgeny.core.network.repository.NetworkRepository
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.feature.user.MainDispatcherRule
import com.mudrichenkoevgeny.feature.user.model.data.getMockUserData
import com.mudrichenkoevgeny.feature.user.network.restapi.user.UserRestApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTests {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: UserRepositoryImpl

    private val networkRepository: NetworkRepository = mockk(relaxed = true)
    private val userPreferences: DataStore<UserPreferences> = mockk(relaxed = true)
    private val authApi: AuthRestApi = mockk(relaxed = true)
    private val userApi: UserRestApi = mockk(relaxed = true)

    @Before
    fun setup() {
        every { userPreferences.data } returns MutableStateFlow(UserPreferences(userData = null))
        coEvery { userPreferences.updateData(any()) } returns UserPreferences(userData = null)
        every { networkRepository.failedToAuthenticateEvent } returns MutableSharedFlow<Unit>()

        userRepository = UserRepositoryImpl(
            networkRepository,
            userPreferences,
            authApi,
            userApi
        )
    }

    @Test
    fun `GIVEN default repository state WHEN failedToAuthenticate() invoked THEN clearAuthData() called 1 times`() = runTest {
        // GIVEN
        val userRepositorySpy = spyk(userRepository)
        // WHEN
        userRepositorySpy.failedToAuthenticate()
        advanceUntilIdle()
        // THEN
        coVerify(exactly = 1) { userRepositorySpy.clearAuthData() }
    }

    @Test
    fun `GIVEN default repository state WHEN logout() invoked THEN clearAuthData() called 1 times`() = runTest {
        // GIVEN
        val userRepositorySpy = spyk(userRepository)
        coEvery { authApi.logout() } returns RestApiResult.Success(Unit)
        // WHEN
        userRepositorySpy.logout()
        advanceUntilIdle()
        // THEN
        coVerify(exactly = 1) { userRepositorySpy.clearAuthData() }
    }

    @Test
    fun `GIVEN userData is not null in preferences WHEN isUserAuthorized() invoked THEN returns true`() = runTest {
        // GIVEN
        val userPreferencesData = flowOf(UserPreferences(userData = getMockUserData()))
        every { userPreferences.data } returns userPreferencesData
        // WHEN
        val result = userRepository.isUserAuthorized()
        // THEN
        assertTrue(result)
    }

    @Test
    fun `GIVEN userData is null in preferences WHEN isUserAuthorized() invoked THEN returns false`() = runTest {
        // GIVEN
        val userPreferencesData = flowOf(UserPreferences(userData = null))
        every { userPreferences.data } returns userPreferencesData
        // WHEN
        val result = userRepository.isUserAuthorized()
        // THEN
        assertFalse(result)
    }

    @Test
    fun `GIVEN successfully completed authApi call WHEN forgotPassword() invoked THEN returns Success result`() = runTest {
        // GIVEN
        coEvery { authApi.forgotPassword(any()) } returns RestApiResult.Success(Unit)
        // WHEN
        val result = userRepository.forgotPassword("test@example.com")
        // THEN
        assertTrue(result is Result.Success)
    }

    @Test
    fun `GIVEN not successfully completed authApi call WHEN forgotPassword() invoked THEN returns Failure result`() = runTest {
        // GIVEN
        coEvery { authApi.forgotPassword(any()) } returns RestApiResult.Error.Exception("exception")
        // WHEN
        val result = userRepository.forgotPassword("test@example.com")
        // THEN
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `GIVEN authResponse with Success result WHEN handleAuthResponse() invoked THEN returns Success result`() = runTest {
        // GIVEN
        val userId = "123"
        val userDataNetwork = UserDataNetwork(id = userId, name = "name", email = "test@example.com")
        val authResponse = RestApiResult.Success(
            AuthResponse(
                userData = userDataNetwork,
                sessionToken = SessionTokenNetwork(accessToken = "", refreshToken = "")
            )
        )
        val userRepositorySpy = spyk(userRepository)
        coEvery { networkRepository.saveSessionTokens(any(), any()) } returns Unit
        // WHEN
        val result = userRepositorySpy.handleAuthResponse(authResponse)
        advanceUntilIdle()
        // THEN
        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { userRepositorySpy.saveAuthData(authResponse.data, userDataNetwork.toUserData()) }
    }

    @Test
    fun `GIVEN authResponse with Error result WHEN handleAuthResponse() invoked THEN returns Failure result`() = runTest {
        // GIVEN
        val authResponse = RestApiResult.Error.Exception("exception")
        val userRepositorySpy = spyk(userRepository)
        // WHEN
        val result = userRepositorySpy.handleAuthResponse(authResponse)
        // THEN
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `GIVEN userData with id in preferences WHEN getUserData() invoked THEN returns UserData with id`() = runTest {
        // GIVEN
        val id = "123"
        val userPreferencesData = flowOf(UserPreferences(userData = getMockUserData(id = id)))
        every { userPreferences.data } returns userPreferencesData
        // WHEN
        val result = userRepository.getUserData()
        // THEN
        assertEquals(id, result?.id)
    }

    @Test
    fun `GIVEN userData is null in preferences WHEN getUserData() invoked THEN returns null`() = runTest {
        // GIVEN
        val userPreferencesData = flowOf(UserPreferences(userData = null))
        every { userPreferences.data } returns userPreferencesData
        // WHEN
        val result = userRepository.getUserData()
        // THEN
        assertNull(result)
    }
}