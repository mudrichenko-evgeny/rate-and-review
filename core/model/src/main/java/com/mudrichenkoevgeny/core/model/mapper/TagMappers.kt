package com.mudrichenkoevgeny.core.model.mapper

import com.mudrichenkoevgeny.core.model.domain.Tag
import com.mudrichenkoevgeny.core.model.network.TagNetwork

fun TagNetwork.toTag(): Tag {
    return Tag(
        id = this.id,
        name = this.name
    )
}