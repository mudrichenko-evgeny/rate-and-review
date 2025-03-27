package com.mudrichenkoevgeny.feature.user.model.data

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserData(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null
)

fun getMockUserData(
    id: String = "userId"
) = UserData(
    id = id,
    name = MockConstants.MOCK_USER_NAME,
    email = MockConstants.MOCK_EMAIL,
    avatarUrl = MockConstants.MOCK_AVATAR_URL
)