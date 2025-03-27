package com.mudrichenkoevgeny.feature.user.network.restapi.user

import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.feature.user.model.network.UserDataNetwork
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserRestApi {

    @DELETE("user/delete/{userId}")
    suspend fun deleteAccount(
        @Path("userId") userId: String
    ): RestApiResult<Unit>

    @Multipart
    @PUT("user/change-user-data/{userId}")
    suspend fun changeUserData(
        @Path("userId") userId: String,
        @Part name: MultipartBody.Part? = null,
        @Part isShouldDeleteAvatar: MultipartBody.Part,
        @Part avatar: MultipartBody.Part? = null
    ): RestApiResult<UserDataNetwork>

    companion object {
        const val PART_NAME = "name"
        const val PART_IS_SHOULD_DELETE_AVATAR = "isShouldDeleteAvatar"
        const val PART_AVATAR = "avatar"
    }
}