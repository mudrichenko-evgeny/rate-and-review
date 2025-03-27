package com.mudrichenkoevgeny.movierating.ui.component.moviecard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mudrichenkoevgeny.core.ui.component.basecard.BaseCard
import com.mudrichenkoevgeny.movierating.model.data.Movie
import com.mudrichenkoevgeny.movierating.model.data.getMockMovie
import com.mudrichenkoevgeny.movierating.ui.component.movieinfobox.MovieInfoBoxWithRatingIndicator
import com.mudrichenkoevgeny.movierating.ui.theme.AppTheme

@Composable
fun MovieCard(
    movie: Movie,
    onClick: (movieId: String) -> Unit
) {
    MovieCardUI(
        id = movie.id,
        nameAndYear = movie.getNameAndYear(),
        duration = movie.getFormattedDuration(),
        tags = movie.getTags(),
        numberOfRatings = movie.numberOfRatings,
        averageRating = movie.averageRating,
        userRating = movie.userReview?.rating,
        imageUrl = movie.imageUrl,
        onClick = onClick
    )
}

@Composable
private fun MovieCardUI(
    id: String,
    nameAndYear: String,
    duration: String,
    tags: String,
    numberOfRatings: Int,
    averageRating: Int?,
    userRating: Int?,
    imageUrl: String?,
    onClick: (movieId: String) -> Unit
) {
    BaseCard(
        onClick = { onClick(id) }
    ) {
        MovieInfoBoxWithRatingIndicator(
            nameAndYear = nameAndYear,
            duration = duration,
            tags = tags,
            numberOfRatings = numberOfRatings,
            averageRating = averageRating,
            userRating = userRating,
            imageUrl = imageUrl,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardDefaultPreview() {
    val movie = getMockMovie(null)
    AppTheme {
        MovieCardUI(
            id = movie.id,
            nameAndYear = movie.getNameAndYear(),
            duration = movie.getFormattedDuration(),
            tags = movie.getTags(),
            numberOfRatings = movie.numberOfRatings,
            averageRating = movie.averageRating,
            userRating = movie.userReview?.rating,
            imageUrl = movie.imageUrl,
            onClick = { }
        )
    }
}