package com.mudrichenkoevgeny.core.network.okhttp

import com.mudrichenkoevgeny.core.network.constants.NetworkConstants.ACCESS_TOKEN_JSON_FIELD_NAME
import com.mudrichenkoevgeny.core.network.constants.NetworkConstants.REFRESH_TOKEN_JSON_FIELD_NAME
import com.mudrichenkoevgeny.core.network.constants.NetworkConstants.REFRESH_TOKEN_URL
import com.mudrichenkoevgeny.core.network.extensions.responseCount
import com.mudrichenkoevgeny.core.network.model.SessionTokenNetwork
import com.mudrichenkoevgeny.core.network.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

const val MAX_AUTHENTICATE_RETRY_COUNT = 3

class OkHttpAuthenticator(
    private val refreshTokenOkHttpClient: OkHttpClient,
    private val networkRepository: NetworkRepository,
    private val baseUrl: String
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.responseCount() >= MAX_AUTHENTICATE_RETRY_COUNT) {
            failedToAuthenticate()
            return null
        }

        val accessToken = runBlocking(Dispatchers.IO) { networkRepository.getSessionAccessToken() }
        if (accessToken == null) {
            failedToAuthenticate()
            return null
        }

        val isTokenAlreadyRefreshed = response.request.header("Authorization")?.contains(accessToken, true) == false
        if (isTokenAlreadyRefreshed) {
            return networkRepository.addAccessTokenToRequest(
                response.request,
                accessToken
            )
        }

        val refreshToken = runBlocking(Dispatchers.IO) { networkRepository.getSessionRefreshToken() }
        if (refreshToken == null) {
            failedToAuthenticate()
            return null
        }

        val newSessionTokenNetwork = refreshTokenSynchronously(refreshToken)
        if (newSessionTokenNetwork == null) {
            failedToAuthenticate()
            return null
        }

        runBlocking(Dispatchers.IO) {
            networkRepository.saveSessionTokens(
                newSessionTokenNetwork.accessToken,
                newSessionTokenNetwork.refreshToken
            )
        }
        return networkRepository.addAccessTokenToRequest(
            response.request,
            newSessionTokenNetwork.accessToken
        )
    }

    private fun refreshTokenSynchronously(refreshToken: String): SessionTokenNetwork? {
        val request = Request.Builder()
            .url("$baseUrl${REFRESH_TOKEN_URL}")
            .post(
                FormBody.Builder()
                    .add(REFRESH_TOKEN_JSON_FIELD_NAME, refreshToken)
                    .build()
            )
            .build()

        return try {
            val response = refreshTokenOkHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody ?: return null)
                val accessToken = jsonObject.getString(ACCESS_TOKEN_JSON_FIELD_NAME)
                val refreshToken = jsonObject.getString(REFRESH_TOKEN_JSON_FIELD_NAME)
                SessionTokenNetwork(accessToken, refreshToken)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun failedToAuthenticate() {
        runBlocking(Dispatchers.IO) { networkRepository.triggerFailedToAuthenticateEvent() }
    }
}