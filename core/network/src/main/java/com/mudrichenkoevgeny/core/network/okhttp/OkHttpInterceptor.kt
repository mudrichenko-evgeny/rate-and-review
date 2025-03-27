package com.mudrichenkoevgeny.core.network.okhttp

import com.mudrichenkoevgeny.core.network.repository.NetworkRepository
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.core.network.util.NetworkChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class OkHttpInterceptor(
    private val networkRepository: NetworkRepository,
    private val networkChecker: NetworkChecker
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkChecker.isInternetAvailable()) {
            throw IOException(RestApiResult.Error.InternetNotAvailable.EXCEPTION_MESSAGE)
        }

        var request = chain.request()

        val token = runBlocking(Dispatchers.IO) { networkRepository.getSessionAccessToken() }
        request = networkRepository.addAccessTokenToRequest(request, token)

        val response = chain.proceed(request)
        return response
    }
}