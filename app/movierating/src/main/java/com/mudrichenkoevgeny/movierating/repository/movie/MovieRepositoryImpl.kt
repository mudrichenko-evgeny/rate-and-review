package com.mudrichenkoevgeny.movierating.repository.movie

import com.mudrichenkoevgeny.core.model.domain.Review
import com.mudrichenkoevgeny.core.model.domain.Tag
import com.mudrichenkoevgeny.core.model.mapper.toReview
import com.mudrichenkoevgeny.core.model.network.TagNetwork
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.movierating.model.data.Genre
import com.mudrichenkoevgeny.movierating.model.data.Movie
import com.mudrichenkoevgeny.movierating.model.mapper.toGenre
import com.mudrichenkoevgeny.movierating.model.mapper.toGenreEntity
import com.mudrichenkoevgeny.movierating.model.mapper.toMovie
import com.mudrichenkoevgeny.movierating.model.mapper.toMovieEntity
import com.mudrichenkoevgeny.movierating.model.mapper.toTag
import com.mudrichenkoevgeny.movierating.model.mapper.toTagEntity
import com.mudrichenkoevgeny.movierating.model.network.GenreNetwork
import com.mudrichenkoevgeny.movierating.model.network.MovieNetwork
import com.mudrichenkoevgeny.movierating.model.network.response.MovieListResponse
import com.mudrichenkoevgeny.movierating.network.requestbody.SaveReviewRequestBody
import com.mudrichenkoevgeny.movierating.network.restapi.MovieRestApi
import com.mudrichenkoevgeny.movierating.result.Result
import com.mudrichenkoevgeny.movierating.result.convertToFailureResult
import com.mudrichenkoevgeny.movierating.storage.database.dao.GenreDao
import com.mudrichenkoevgeny.movierating.storage.database.dao.MovieDao
import com.mudrichenkoevgeny.movierating.storage.database.dao.TagDao
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlin.collections.map

class MovieRepositoryImpl @Inject constructor(
    private val tagDao: TagDao,
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val movieRestApi: MovieRestApi
) : MovieRepository {

    private var isSynced: Boolean = false

    override suspend fun syncContent(): Result<Unit> = coroutineScope {
        val tagsResultDeferred = async { movieRestApi.getTags() }
        val genresResultDeferred = async { movieRestApi.getGenres() }

        val tagsResult = tagsResultDeferred.await()
        val genresResult = genresResultDeferred.await()

        if (tagsResult is RestApiResult.Error) {
            return@coroutineScope tagsResult.convertToFailureResult()
        } else if (tagsResult is RestApiResult.Success) {
            handleTagsResult(tagsResult.data)
        }

        if (genresResult is RestApiResult.Error) {
            return@coroutineScope genresResult.convertToFailureResult()
        } else if (genresResult  is RestApiResult.Success) {
            handleGenresResult(genresResult.data)
        }

        return@coroutineScope Result.Success(Unit)
    }

    private suspend fun handleTagsResult(tagNetworkList: List<TagNetwork>) {
        tagDao.clear()
        tagDao.insertAll(tagNetworkList.map { it.toTagEntity() })
    }

    private suspend fun handleGenresResult(genreNetworkList: List<GenreNetwork>) {
        genreDao.clear()
        genreDao.insertAll(genreNetworkList.map { it.toGenreEntity() })
    }

    override suspend fun getGlobalFeed(page: Int): Result<List<Movie>> {
        checkAndSyncContent()

        return handleMovieListResponse(
            response = movieRestApi.getGlobalFeed(page)
        )
    }

    override suspend fun getUserFeed(page: Int): Result<List<Movie>> {
        checkAndSyncContent()

        return handleMovieListResponse(
            response = movieRestApi.getUserFeed(page)
        )
    }

    override suspend fun getTags(): Result<List<Tag>> {
        return Result.Success(tagDao.getAll().map { it.toTag() })
    }

    override suspend fun getGenres(): Result<List<Genre>> {
        return Result.Success(genreDao.getAll().map { it.toGenre() })
    }

    override suspend fun getMovie(id: String): Result<Movie> {
        val movieEntity = movieDao.getById(id)
        if (movieEntity != null) {
            return Result.Success(movieEntity.toMovie())
        }
        val movieResponse = movieRestApi.getMovie(id)
        return when (movieResponse) {
            is RestApiResult.Error -> {
                movieResponse.convertToFailureResult()
            }
            is RestApiResult.Success -> {
                Result.Success(movieResponse.data.toMovie())
            }
        }
    }

    override suspend fun getMovieReviews(movieId: String, page: Int): Result<List<Review>> {
        val reviewsResponse = movieRestApi.getReviewList(movieId, page)
        return when (reviewsResponse) {
            is RestApiResult.Error -> {
                reviewsResponse.convertToFailureResult()
            }
            is RestApiResult.Success -> {
                Result.Success(reviewsResponse.data.reviews.map { it.toReview() })
            }
        }
    }

    override suspend fun saveReview(
        movieId: String,
        rating: Int,
        reviewText: String
    ): Result<Review> {
        val response = movieRestApi.saveReview(
            SaveReviewRequestBody(
                movieId = movieId,
                rating = rating,
                reviewText = reviewText
            )
        )
        return when (response) {
            is RestApiResult.Error -> {
                response.convertToFailureResult()
            }
            is RestApiResult.Success -> {
                Result.Success(response.data.toReview())
            }
        }
    }

    override suspend fun deleteReview(reviewId: String): Result<Unit> {
        val response = movieRestApi.deleteReview(reviewId)
        return when (response) {
            is RestApiResult.Error -> {
                response.convertToFailureResult()
            }
            is RestApiResult.Success -> {
                Result.Success(Unit)
            }
        }
    }

    private suspend fun checkAndSyncContent(): Result<Unit> {
        return if (!isSynced) {
            val syncResult = syncContent()
            if (syncResult is Result.Success) {
                isSynced = true
            }
            syncResult
        } else {
            Result.Success(Unit)
        }
    }

    private suspend fun handleMovieListResponse(
        response: RestApiResult<MovieListResponse>
    ): Result<List<Movie>> {
        return when (response) {
            is RestApiResult.Error -> {
                response.convertToFailureResult()
            }
            is RestApiResult.Success -> {
                saveMovieListLocally(response.data.movies)
                Result.Success(response.data.movies.map { it.toMovie() })
            }
        }
    }

    private suspend fun saveMovieListLocally(movieList: List<MovieNetwork>) {
        movieDao.insertAll(movieList.map { it.toMovieEntity() })
    }
}