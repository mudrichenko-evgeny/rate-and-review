package com.mudrichenkoevgeny.feature.user.model.mapper

import com.mudrichenkoevgeny.feature.user.model.data.UserData
import com.mudrichenkoevgeny.feature.user.model.network.UserDataNetwork

fun UserDataNetwork.toUserData(): UserData {
    return UserData(
        id = this.id,
        name = this.name,
        email = this.email,
        avatarUrl = this.avatarUrl
    )
}