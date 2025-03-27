package com.mudrichenkoevgeny.core.network.repository

import androidx.datastore.core.DataStore
import com.mudrichenkoevgeny.core.network.datastore.NetworkSessionData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.Request
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkSessionDataStore: DataStore<NetworkSessionData>
) : NetworkRepository {

    private val _failedToAuthenticateEvent = MutableSharedFlow<Unit>(replay = 1)
    override val failedToAuthenticateEvent = _failedToAuthenticateEvent.asSharedFlow()

    override suspend fun getSessionAccessToken(): String? {
        return networkSessionDataStore.data.firstOrNull()?.accessToken
    }

    override suspend fun getSessionRefreshToken(): String? {
        return networkSessionDataStore.data.firstOrNull()?.refreshToken
    }

    override suspend fun saveSessionTokens(accessToken: String, refreshToken: String) {
        networkSessionDataStore.updateData { sessionPreferences ->
            sessionPreferences.copy(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }

    override suspend fun clearSession() {
        networkSessionDataStore.updateData { sessionPreferences ->
            sessionPreferences.copy(
                accessToken = null,
                refreshToken = null
            )
        }
    }

    override fun addAccessTokenToRequest(request: Request, accessToken: String?): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }

    override suspend fun triggerFailedToAuthenticateEvent() {
        _failedToAuthenticateEvent.emit(Unit)
    }
}