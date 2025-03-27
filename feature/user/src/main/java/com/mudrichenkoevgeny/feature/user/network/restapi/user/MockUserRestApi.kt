package com.mudrichenkoevgeny.feature.user.network.restapi.user

import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.feature.user.model.network.UserDataNetwork
import kotlinx.coroutines.delay
import okhttp3.MultipartBody

class MockUserRestApi(
    val simulateDelay: Boolean = true
) : UserRestApi {

    private val userDataNetwork = UserDataNetwork(
        id = "userId",
        name = MockConstants.MOCK_USER_NAME,
        email = MockConstants.MOCK_EMAIL,
        avatarUrl = MockConstants.MOCK_AVATAR_URL
    )

    override suspend fun deleteAccount(userId: String): RestApiResult<Unit> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(Unit)
    }

    override suspend fun changeUserData(
        userId: String,
        name: MultipartBody.Part?,
        isShouldDeleteAvatar: MultipartBody.Part,
        avatar: MultipartBody.Part?
    ): RestApiResult<UserDataNetwork> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(userDataNetwork)
    }

    companion object {
        const val FAKE_DELAY_MS = 1_000L
    }
}