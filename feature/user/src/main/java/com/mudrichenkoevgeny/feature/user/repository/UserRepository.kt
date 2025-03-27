package com.mudrichenkoevgeny.feature.user.repository

import com.mudrichenkoevgeny.feature.user.model.data.UserData
import com.mudrichenkoevgeny.feature.user.result.Result
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    val userDataFlow: Flow<UserData?>

    suspend fun isUserAuthorized(): Boolean
    suspend fun getUserData(): UserData?
    suspend fun login(email: String, password: String): Result<UserData>
    suspend fun register(email: String, password: String, name: String): Result<UserData>
    suspend fun logout(): Result<Unit>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun saveUserData(name: String?, isShouldDeleteAvatar: Boolean, avatarFile: File?): Result<Unit>
}