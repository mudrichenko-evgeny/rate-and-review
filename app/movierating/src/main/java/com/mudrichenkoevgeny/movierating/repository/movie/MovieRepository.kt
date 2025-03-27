package com.mudrichenkoevgeny.movierating.repository.movie

import com.mudrichenkoevgeny.core.model.domain.Review
import com.mudrichenkoevgeny.core.model.domain.Tag
import com.mudrichenkoevgeny.movierating.model.data.Genre
import com.mudrichenkoevgeny.movierating.model.data.Movie
import com.mudrichenkoevgeny.movierating.result.Result

interface MovieRepository {
    suspend fun syncContent(): Result<Unit>
    suspend fun getGlobalFeed(page: Int): Result<List<Movie>>
    suspend fun getUserFeed(page: Int): Result<List<Movie>>
    suspend fun getTags(): Result<List<Tag>>
    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getMovie(id: String): Result<Movie>
    suspend fun getMovieReviews(movieId: String, page: Int): Result<List<Review>>
    suspend fun saveReview(movieId: String, rating: Int, reviewText: String): Result<Review>
    suspend fun deleteReview(reviewId: String): Result<Unit>
}