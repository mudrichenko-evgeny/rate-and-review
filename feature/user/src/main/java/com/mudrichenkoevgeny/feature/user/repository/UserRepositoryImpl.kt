package com.mudrichenkoevgeny.feature.user.repository

import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import com.mudrichenkoevgeny.core.network.constants.NetworkConstants
import com.mudrichenkoevgeny.feature.user.datastore.UserPreferences
import com.mudrichenkoevgeny.feature.user.model.data.UserData
import com.mudrichenkoevgeny.feature.user.model.mapper.toUserData
import com.mudrichenkoevgeny.feature.user.model.network.response.AuthResponse
import com.mudrichenkoevgeny.feature.user.network.restapi.auth.AuthRestApi
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.AuthRequestBody
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.ForgotPasswordRequestBody
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.RegistrationRequestBody
import com.mudrichenkoevgeny.feature.user.result.Result
import com.mudrichenkoevgeny.feature.user.result.convertToFailureResult
import com.mudrichenkoevgeny.core.network.repository.NetworkRepository
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.feature.user.network.restapi.user.UserRestApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val userPreferences: DataStore<UserPreferences>,
    private val authApi: AuthRestApi,
    private val userRestApi: UserRestApi
) : UserRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            networkRepository.failedToAuthenticateEvent.collectLatest {
                failedToAuthenticate()
            }
        }
    }

    override val userDataFlow: Flow<UserData?> = userPreferences.data
        .map { it.userData }
        .distinctUntilChanged()

    override suspend fun isUserAuthorized(): Boolean {
        return userPreferences.data.firstOrNull()?.userData != null
    }

    override suspend fun getUserData(): UserData? {
        return userPreferences.data.firstOrNull()?.userData
    }

    override suspend fun login(email: String, password: String): Result<UserData> {
        return handleAuthResponse(
            authApi.login(AuthRequestBody(email = email, password = password))
        )
    }

    override suspend fun register(email: String, password: String, name: String): Result<UserData> {
        return handleAuthResponse(
            authApi.register(RegistrationRequestBody(email = email, password = password, name = name))
        )
    }

    override suspend fun logout(): Result<Unit> {
        authApi.logout()
        clearAuthData()
        return Result.Success(Unit)
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        val response = authApi.forgotPassword(ForgotPasswordRequestBody(email = email))
        return when (response) {
            is RestApiResult.Success -> {
                Result.Success(Unit)
            }
            is RestApiResult.Error -> {
                response.convertToFailureResult()
            }
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        val userId = getUserData()?.id
        if (userId == null) {
            return Result.Failure.Unknown
        }
        val response = userRestApi.deleteAccount(userId)
        return when (response) {
            is RestApiResult.Success -> {
                clearAuthData()
                Result.Success(Unit)
            }
            is RestApiResult.Error -> {
                response.convertToFailureResult()
            }
        }
    }

    override suspend fun saveUserData(name: String?, isShouldDeleteAvatar: Boolean, avatarFile: File?): Result<Unit> {
        val userId = getUserData()?.id
        if (userId == null) {
            return Result.Failure.Unknown
        }

        val namePart = name?.let { name ->
            val requestBody = name.toRequestBody(NetworkConstants.MEDIA_TYPE_TEXT.toMediaTypeOrNull())
            MultipartBody.Part.createFormData(UserRestApi.PART_NAME, null, requestBody)
        }

        val requestBody = isShouldDeleteAvatar.toString().toRequestBody(NetworkConstants.MEDIA_TYPE_TEXT.toMediaTypeOrNull())
        val isShouldDeleteAvatarPart = MultipartBody.Part.createFormData(UserRestApi.PART_IS_SHOULD_DELETE_AVATAR, null, requestBody)

        val avatarPart = avatarFile?.let { avatarFile ->
            val requestFile = avatarFile.asRequestBody(NetworkConstants.MEDIA_TYPE_IMAGE.toMediaTypeOrNull())
            MultipartBody.Part.createFormData(UserRestApi.PART_AVATAR, null, requestFile)
        }

        val response = userRestApi.changeUserData(
            userId = userId,
            isShouldDeleteAvatar = isShouldDeleteAvatarPart,
            name = namePart,
            avatar = avatarPart
        )
        return when (response) {
            is RestApiResult.Success -> {
                val userData = response.data.toUserData()
                saveUserData(userData)
                Result.Success(Unit)
            }
            is RestApiResult.Error -> {
                response.convertToFailureResult()
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleAuthResponse(
        authResponse: RestApiResult<AuthResponse>
    ): Result<UserData> {
        return when (authResponse) {
            is RestApiResult.Success -> {
                val userData = authResponse.data.userData.toUserData()
                saveAuthData(authResponse.data, userData)
                Result.Success(userData)
            }
            is RestApiResult.Error -> {
                authResponse.convertToFailureResult()
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveAuthData(authResponse: AuthResponse, userData: UserData) {
        networkRepository.saveSessionTokens(
            accessToken = authResponse.sessionToken.accessToken,
            refreshToken = authResponse.sessionToken.refreshToken
        )
        saveUserData(userData)
    }

    private suspend fun saveUserData(userData: UserData) {
        userPreferences.updateData { authPreferences ->
            authPreferences.copy(userData = userData)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun failedToAuthenticate() {
        clearAuthData()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun clearAuthData() {
        userPreferences.updateData { authPreferences ->
            authPreferences.copy(userData = null)
        }
    }
}