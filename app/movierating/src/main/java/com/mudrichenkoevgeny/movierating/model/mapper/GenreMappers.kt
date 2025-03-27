package com.mudrichenkoevgeny.movierating.model.mapper

import com.mudrichenkoevgeny.movierating.model.data.Genre
import com.mudrichenkoevgeny.movierating.model.network.GenreNetwork
import com.mudrichenkoevgeny.movierating.storage.database.model.GenreEntity

fun GenreNetwork.toGenre(): Genre {
    return Genre(
        id = this.id,
        name = this.name
    )
}

fun GenreNetwork.toGenreEntity(): GenreEntity {
    return GenreEntity(
        id = this.id,
        name = this.name
    )
}

fun GenreEntity.toGenre(): Genre {
    return Genre(
        id = this.id,
        name = this.name
    )
}