package com.mudrichenkoevgeny.movierating.model.mapper

import com.mudrichenkoevgeny.core.model.domain.Tag
import com.mudrichenkoevgeny.core.model.network.TagNetwork
import com.mudrichenkoevgeny.movierating.storage.database.model.TagEntity

fun TagNetwork.toTagEntity(): TagEntity {
    return TagEntity(
        id = this.id,
        name = this.name
    )
}

fun TagEntity.toTag(): Tag {
    return Tag(
        id = this.id,
        name = this.name
    )
}