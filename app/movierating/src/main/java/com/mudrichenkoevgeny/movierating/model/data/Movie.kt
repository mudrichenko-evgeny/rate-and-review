package com.mudrichenkoevgeny.movierating.model.data

import com.mudrichenkoevgeny.core.common.util.convertMinutesToDurationTime
import com.mudrichenkoevgeny.core.model.domain.Review
import com.mudrichenkoevgeny.core.model.domain.ReviewableItem
import com.mudrichenkoevgeny.core.model.domain.Tag
import com.mudrichenkoevgeny.core.model.domain.getMockTag

data class Movie(
    override val id: String,
    override val name: String,
    override val imageUrl: String?,
    override val averageRating: Int?,
    override val numberOfRatings: Int,
    override val userReview: Review?,
    override val tags: List<Tag>,
    val releaseYear: Int,
    val durationInMinutes: Int,
    val genres: List<Genre>
) : ReviewableItem(
    id = id,
    name = name,
    imageUrl = imageUrl,
    averageRating = averageRating,
    numberOfRatings = numberOfRatings,
    userReview = userReview,
    tags = tags
) {

    fun getNameAndYear(): String {
        return "$name ($releaseYear)"
    }

    fun getFormattedDuration(): String {
        return durationInMinutes.convertMinutesToDurationTime()
    }

    fun getTags(): String = tags.joinToString(", ") { it.name }

    fun getGenres(): String = genres.joinToString(", ") { it.name }
}

fun getMockMovie(userReview: Review?) = Movie(
    id = "1",
    name = "Movie name",
    imageUrl = "",
    averageRating = 81,
    numberOfRatings = 1548,
    userReview = userReview,
    tags = listOf(getMockTag()),
    releaseYear = 1997,
    durationInMinutes = 131,
    genres = listOf(getMockGenre())
)