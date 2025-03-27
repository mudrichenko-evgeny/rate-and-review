package com.mudrichenkoevgeny.movierating.ui.component.movieinfobox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mudrichenkoevgeny.core.ui.component.ratingindicator.RatingIndicator
import com.mudrichenkoevgeny.core.ui.extensions.addPadding
import com.mudrichenkoevgeny.movierating.R
import com.mudrichenkoevgeny.movierating.config.AppConfig
import com.mudrichenkoevgeny.movierating.model.data.getMockMovie
import com.mudrichenkoevgeny.movierating.ui.theme.AppTheme
import com.mudrichenkoevgeny.core.ui.R as UIResources

@Composable
fun MovieInfoBoxWithRatingIndicator(
    nameAndYear: String,
    duration: String,
    tags: String,
    numberOfRatings: Int = 0,
    averageRating: Int? = null,
    userRating: Int? = null,
    imageUrl: String?
) {
    MovieInfoBox(
        movieInfoBoxType = MovieInfoBoxType.WithRatingIndicator,
        nameAndYear = nameAndYear,
        duration = duration,
        tags = tags,
        numberOfRatings = numberOfRatings,
        averageRating = averageRating,
        userRating = userRating,
        imageUrl = imageUrl
    )
}

@Composable
fun MovieInfoBoxWithGenresList(
    nameAndYear: String,
    duration: String,
    tags: String,
    genres: String,
    imageUrl: String?
) {
    MovieInfoBox(
        movieInfoBoxType = MovieInfoBoxType.WithGenresList,
        nameAndYear = nameAndYear,
        duration = duration,
        tags = tags,
        genres = genres,
        imageUrl = imageUrl
    )
}

@Composable
private fun MovieInfoBox(
    movieInfoBoxType: MovieInfoBoxType,
    nameAndYear: String,
    duration: String,
    tags: String,
    genres: String = "",
    numberOfRatings: Int = 0,
    averageRating: Int? = null,
    userRating: Int? = null,
    imageUrl: String?
) {
    Row(
        modifier = Modifier
            .padding(dimensionResource(UIResources.dimen.card_padding))
            .fillMaxWidth()
            .layout { measurable, constraints ->
                val width = constraints.maxWidth
                val height = width / 3
                val newConstraints = constraints.copy(minHeight = height, maxHeight = height)
                val placeable = measurable.measure(newConstraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .fillMaxHeight()
                .aspectRatio(3f / 4.5f)
        ) {
            val placeholder = painterResource(R.drawable.ic_movie_placeholder)
                .addPadding(dimensionResource(R.dimen.movie_item_placeholder_image_padding))
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                placeholder = placeholder,
                error = placeholder,
                contentDescription = stringResource(R.string.cd_movie_image),
            )
        }
        Spacer(
            modifier = Modifier.width(dimensionResource(R.dimen.movie_item_content_padding))
        )
        Column(
            modifier = Modifier.weight(0.8f)
                .padding(top = dimensionResource(R.dimen.movie_item_content_padding))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = nameAndYear,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(
                    modifier = Modifier.width(dimensionResource(R.dimen.movie_item_content_padding))
                )
                Text(
                    text = duration,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text(
                text = tags,
                modifier = Modifier.weight(1f)
                    .padding(top = dimensionResource(R.dimen.movie_item_content_padding)),
                textAlign = TextAlign.Start,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onPrimary
            )
            when (movieInfoBoxType) {
                MovieInfoBoxType.WithRatingIndicator -> {
                    RatingIndicator(
                        modifier = Modifier.align(Alignment.End),
                        numberOfRatings = numberOfRatings,
                        averageRating = averageRating,
                        userRating = userRating,
                        maxRating = AppConfig.MAX_RATING
                    )
                }
                MovieInfoBoxType.WithGenresList -> {
                    Text(
                        text = genres,
                        modifier = Modifier
                            .padding(top = dimensionResource(R.dimen.movie_item_content_padding)),
                        textAlign = TextAlign.Start,
                        fontStyle = FontStyle.Italic,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieInfoBoxWithRatingIndicatorPreview() {
    val movie = getMockMovie(null)
    AppTheme {
        MovieInfoBoxWithRatingIndicator(
            nameAndYear = movie.getNameAndYear(),
            duration = movie.getFormattedDuration(),
            tags = movie.getTags(),
            numberOfRatings = movie.numberOfRatings,
            averageRating = movie.averageRating,
            userRating = movie.userReview?.rating,
            imageUrl = movie.imageUrl
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieInfoBoxWithGenresListPreview() {
    val movie = getMockMovie(null)
    AppTheme {
        MovieInfoBoxWithGenresList(
            nameAndYear = movie.getNameAndYear(),
            duration = movie.getFormattedDuration(),
            tags = movie.getTags(),
            genres = movie.getGenres(),
            imageUrl = movie.imageUrl
        )
    }
}

sealed class MovieInfoBoxType {
    object WithRatingIndicator : MovieInfoBoxType()
    object WithGenresList : MovieInfoBoxType()
}