package com.mudrichenkoevgeny.movierating.storage.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mudrichenkoevgeny.core.model.domain.Review

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String?,
    val averageRating: Int?,
    val numberOfRatings: Int,
    val userReview: Review?,
    val tags: List<TagEntity>,
    val releaseYear: Int,
    val durationInMinutes: Int,
    val genres: List<GenreEntity>
)