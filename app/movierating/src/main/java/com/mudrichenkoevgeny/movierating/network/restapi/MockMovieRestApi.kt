package com.mudrichenkoevgeny.movierating.network.restapi

import com.mudrichenkoevgeny.core.common.constants.MockConstants
import com.mudrichenkoevgeny.core.model.network.ReviewNetwork
import com.mudrichenkoevgeny.core.model.network.ReviewerNetwork
import com.mudrichenkoevgeny.core.model.network.TagNetwork
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.feature.user.model.data.UserData
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.movierating.model.network.GenreNetwork
import com.mudrichenkoevgeny.movierating.model.network.MovieNetwork
import com.mudrichenkoevgeny.movierating.model.network.response.MovieListResponse
import com.mudrichenkoevgeny.movierating.model.network.response.ReviewListResponse
import com.mudrichenkoevgeny.movierating.network.requestbody.SaveReviewRequestBody
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random

class MockMovieRestApi(
    val userRepository: UserRepository,
    val simulateDelay: Boolean = true,
    val pageWithError: Int? = 3
) : MovieRestApi {

    private val tags: List<TagNetwork> = listOf(
        TagNetwork(
            id = "1",
            name = "Funny"
        ),
        TagNetwork(
            id = "2",
            name = "Scary"
        ),
        TagNetwork(
            id = "3",
            name = "Classic"
        )
    )

    private val genres: List<GenreNetwork> = listOf(
        GenreNetwork(
            id = "1",
            name = "Comedy"
        ),
        GenreNetwork(
            id = "2",
            name = "Thriller"
        ),
        GenreNetwork(
            id = "3",
            name = "Drama"
        )
    )

    val words = listOf(
        "apple", "banana", "cherry", "dog", "elephant", "flower", "guitar", "house", "ice", "jungle"
    )

    val firstNames = listOf(
        "Ethan", "Olivia", "Noah", "Ava", "Liam", "Sophia", "Mason", "Isabella", "Lucas", "Charlotte"
    )

    val secondNames = listOf(
        "Carter", "Bennett", "Mitchell", "Sullivan", "Henderson", "Reed", "Brooks", "Hayes", "Turner", "Dawson"
    )

    val filmNameFirstPart = listOf(
        "Lost", "Dark", "Golden", "Silent", "Hidden", "Brave", "Secret", "Last", "Broken", "Endless"
    )
    val filmNameSecondPart = listOf(
        "Journey", "Empire", "Dream", "Shadow", "Legend", "World", "Path", "Chronicle", "Horizon", "Saga"
    )

    override suspend fun getTags(): RestApiResult<List<TagNetwork>> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(tags)
    }

    override suspend fun getGenres(): RestApiResult<List<GenreNetwork>> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(genres)
    }

    override suspend fun getGlobalFeed(page: Int): RestApiResult<MovieListResponse> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        if (page == pageWithError) {
            return RestApiResult.Error.Exception("exception")
        }
        return RestApiResult.Success(
            MovieListResponse(
                page = page,
                movies = generateMovieList(
                    userData = null,
                    isWithUserReviews = false
                )
            )
        )
    }

    override suspend fun getUserFeed(page: Int): RestApiResult<MovieListResponse> {
        val userData = userRepository.getUserData()
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        if (page == pageWithError) {
            return RestApiResult.Error.Exception("exception")
        }
        return RestApiResult.Success(
            MovieListResponse(
                page = page,
                movies = generateMovieList(
                    userData = userData,
                    isWithUserReviews = true
                )
            )
        )
    }

    override suspend fun getMovie(id: String): RestApiResult<MovieNetwork> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(
            getMovieNetwork(
                movieId = null,
                userData = null,
                isWithUserReviews = false
            )
        )
    }

    override suspend fun getReviewList(movieId: String, page: Int): RestApiResult<ReviewListResponse> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        if (page == pageWithError) {
            return RestApiResult.Error.Exception("exception")
        }
        return RestApiResult.Success(
            ReviewListResponse(
                page = page,
                reviews = generateReviewList(
                    movieId = movieId
                )
            )
        )
    }

    override suspend fun saveReview(request: SaveReviewRequestBody): RestApiResult<ReviewNetwork> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        val userData = userRepository.getUserData()
        return RestApiResult.Success(
            getReview(
                movieId = request.movieId,
                reviewerId = userData?.id ?: "",
                reviewerName = userData?.name ?: "",
                rating = request.rating,
                reviewText = request.reviewText
            )
        )
    }

    override suspend fun deleteReview(id: String): RestApiResult<Unit> {
        if (simulateDelay) {
            delay(FAKE_DELAY_MS)
        }
        return RestApiResult.Success(Unit)
    }

    private fun generateMovieList(
        size: Int = 10,
        userData: UserData?,
        isWithUserReviews: Boolean
    ): List<MovieNetwork> {
        return List(size) {
            getMovieNetwork(
                movieId = null,
                userData = userData,
                isWithUserReviews = isWithUserReviews
            )
        }
    }

    private fun generateReviewList(
        size: Int = 10,
        movieId: String
    ): List<ReviewNetwork> {
        return List(size) {
            getReview(
                movieId = movieId,
                reviewerId = null,
                reviewerName = null
            )
        }
    }

    @Suppress("SameParameterValue")
    private fun getMovieNetwork(
        movieId: String? = null,
        userData: UserData?,
        isWithUserReviews: Boolean
    ): MovieNetwork {
        val movieNetworkId = movieId ?: UUID.randomUUID().toString()
        return MovieNetwork(
            id = movieNetworkId,
            name = getRandomFilmName(),
            imageUrl = MockConstants.MOCK_IMAGE_URL,
            averageRating = Random.nextInt(1, 101),
            numberOfRatings = Random.nextInt(1, 1001),
            userReview = if (isWithUserReviews && userData != null) {
                getReview(
                    movieId = movieNetworkId,
                    reviewerId = userData.id,
                    reviewerName = userData.name
                )
            } else {
                null
            },
            tags = listOf(tags.random()),
            releaseYear = Random.nextInt(1960, 2025),
            durationInMinutes = Random.nextInt(45, 199),
            genres = listOf(genres.random())
        )
    }

    private fun getReview(
        movieId: String,
        reviewerId: String? = null,
        reviewerName: String? = null,
        rating: Int? = null,
        reviewText: String? = null
    ): ReviewNetwork {
        return ReviewNetwork(
            id = UUID.randomUUID().toString(),
            reviewableItemId = movieId,
            reviewer = getReviewer(
                reviewerId = reviewerId ?: UUID.randomUUID().toString(),
                reviewerName = reviewerName ?: getRandomUserName()
            ),
            rating = rating ?: Random.nextInt(1, 101),
            reviewText = reviewText ?: getRandomText(Random.nextInt(10, 101)),
            createdAt = Random.nextLong(1_735_678_800_000, 1_738_357_200_000)
        )
    }

    private fun getReviewer(
        reviewerId: String,
        reviewerName: String
    ): ReviewerNetwork {
        return ReviewerNetwork(
            id = reviewerId,
            name = reviewerName,
            avatarUrl = MockConstants.MOCK_AVATAR_URL,
            numberOfReviews = Random.nextInt(1, 101),
            averageRating = Random.nextInt(1, 101)
        )
    }

    private fun getRandomUserName(): String {
        return "${firstNames.random()} ${secondNames.random()}"
    }

    private fun getRandomFilmName(): String {
        return "${filmNameFirstPart.random()} ${filmNameSecondPart.random()}"
    }

    private fun getRandomText(numberOfWords: Int): String {
        return List(numberOfWords) { words.random() }.joinToString(" ")
    }

    companion object {
        const val FAKE_DELAY_MS = 1_000L
    }
}