package com.mudrichenkoevgeny.feature.user.network.restapi.auth

import com.mudrichenkoevgeny.feature.user.model.network.response.AuthResponse
import com.mudrichenkoevgeny.feature.user.model.network.UserDataNetwork
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.AuthRequestBody
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.ForgotPasswordRequestBody
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.RegistrationRequestBody
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.network.model.SessionTokenNetwork
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import kotlinx.coroutines.delay

class MockAuthRestApi(
    val simulateDelay: Boolean = true
) : AuthRestApi {

    private val userDataNetwork = UserDataNetwork(
        id = "userId",
        name = MockConstants.MOCK_USER_NAME,
        email = MockConstants.MOCK_EMAIL,
        avatarUrl = MockConstants.MOCK_AVATAR_URL
    )
    private val sessionTokenNetwork = SessionTokenNetwork(
        accessToken = "123456",
        refreshToken = "123456"
    )

    override suspend fun register(request: RegistrationRequestBody): RestApiResult<AuthResponse> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(AuthResponse(userDataNetwork, sessionTokenNetwork))
    }

    override suspend fun login(request: AuthRequestBody): RestApiResult<AuthResponse> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(AuthResponse(userDataNetwork, sessionTokenNetwork))
    }

    override suspend fun logout(): RestApiResult<Unit> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(Unit)
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequestBody): RestApiResult<Unit> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(Unit)
    }

    companion object {
        const val FAKE_DELAY_MS = 1_000L
    }
}