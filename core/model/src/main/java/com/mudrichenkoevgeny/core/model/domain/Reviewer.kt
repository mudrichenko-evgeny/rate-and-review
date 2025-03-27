package com.mudrichenkoevgeny.core.model.domain

import android.annotation.SuppressLint
import com.mudrichenkoevgeny.core.common.constants.MockConstants
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Reviewer(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val numberOfReviews: Int,
    val averageRating: Int?
)

fun getMockReviewer() = Reviewer(
    id = "1",
    name = MockConstants.MOCK_USER_NAME,
    avatarUrl = null,
    numberOfReviews = 11,
    averageRating = 68
)