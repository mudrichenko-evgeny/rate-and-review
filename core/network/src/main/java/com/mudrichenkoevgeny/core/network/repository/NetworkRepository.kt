package com.mudrichenkoevgeny.core.network.repository

import kotlinx.coroutines.flow.SharedFlow
import okhttp3.Request

interface NetworkRepository {
    val failedToAuthenticateEvent: SharedFlow<Unit>
    suspend fun getSessionAccessToken(): String?
    suspend fun getSessionRefreshToken(): String?
    suspend fun saveSessionTokens(accessToken: String, refreshToken: String)
    suspend fun clearSession()
    fun addAccessTokenToRequest(request: Request, accessToken: String?): Request
    suspend fun triggerFailedToAuthenticateEvent()
}