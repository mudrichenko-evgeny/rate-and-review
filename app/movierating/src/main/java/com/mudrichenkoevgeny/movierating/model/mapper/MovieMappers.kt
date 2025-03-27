package com.mudrichenkoevgeny.movierating.model.mapper

import com.mudrichenkoevgeny.core.model.mapper.toReview
import com.mudrichenkoevgeny.core.model.mapper.toTag
import com.mudrichenkoevgeny.movierating.model.data.Movie
import com.mudrichenkoevgeny.movierating.model.network.MovieNetwork
import com.mudrichenkoevgeny.movierating.storage.database.model.MovieEntity

fun MovieNetwork.toMovie(): Movie {
    return Movie(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        averageRating = this.averageRating,
        numberOfRatings = this.numberOfRatings,
        userReview = this.userReview?.toReview(),
        tags = this.tags.map { it.toTag() },
        releaseYear = this.releaseYear,
        durationInMinutes = this.durationInMinutes,
        genres = this.genres.map { it.toGenre() }
    )
}

fun MovieNetwork.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        averageRating = this.averageRating,
        numberOfRatings = this.numberOfRatings,
        userReview = this.userReview?.toReview(),
        tags = this.tags.map { it.toTagEntity() },
        releaseYear = this.releaseYear,
        durationInMinutes = this.durationInMinutes,
        genres = this.genres.map { it.toGenreEntity() }
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        averageRating = this.averageRating,
        numberOfRatings = this.numberOfRatings,
        userReview = this.userReview,
        tags = this.tags.map { it.toTag() },
        releaseYear = this.releaseYear,
        durationInMinutes = this.durationInMinutes,
        genres = this.genres.map { it.toGenre() }
    )
}