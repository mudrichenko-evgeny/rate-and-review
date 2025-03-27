package com.mudrichenkoevgeny.feature.user.network.restapi.auth

import com.mudrichenkoevgeny.feature.user.model.network.response.AuthResponse
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.AuthRequestBody
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.ForgotPasswordRequestBody
import com.mudrichenkoevgeny.feature.user.network.restapi.requestbody.RegistrationRequestBody
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRestApi {

    @POST("auth/register")
    suspend fun register(@Body request: RegistrationRequestBody): RestApiResult<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequestBody): RestApiResult<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(): RestApiResult<Unit>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequestBody): RestApiResult<Unit>
}