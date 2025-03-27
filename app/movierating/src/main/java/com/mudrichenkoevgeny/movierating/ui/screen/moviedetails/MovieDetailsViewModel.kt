package com.mudrichenkoevgeny.movierating.ui.screen.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.feature.user.repository.UserRepository
import com.mudrichenkoevgeny.movierating.repository.movie.MovieRepository
import com.mudrichenkoevgeny.movierating.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(MovieDetailsUiState(isMovieLoading = true))
    val uiState: StateFlow<MovieDetailsUiState> = _uiState

    private val _showAuthDialogRequest = MutableSharedFlow<Unit>()
    val showAuthDialogRequest: SharedFlow<Unit> = _showAuthDialogRequest

    private val _showEditReviewDialogRequest = MutableSharedFlow<Unit>()
    val showEditReviewDialogRequest: SharedFlow<Unit> = _showEditReviewDialogRequest

    private var reviewListLastLoadedPage: Int = 0
    private var isReviewListLoading: Boolean = false

    fun onLoadMovieDetails(movieId: String) {
        viewModelScope.launch {
            val movieResult = movieRepository.getMovie(movieId)
            when (movieResult) {
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        movie = null,
                        isMovieLoading = false,
                        movieLoadingError = movieResult
                    )
                }
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        movie = movieResult.data,
                        isMovieLoading = false,
                        movieLoadingError = null,
                        userReview = movieResult.data.userReview,
                        userReviewEditLoading = false,
                        userReviewEditError = null
                    )
                    loadReviewList(pageNumber = 1)
                }
            }
        }
    }

    fun loadReviewList(pageNumber: Int? = null) {
        if (isReviewListLoading) {
            return
        }
        val movieId = _uiState.value.movie?.id ?: return
        val page = if (pageNumber == null) {
            reviewListLastLoadedPage + 1
        } else {
            pageNumber
        }
        isReviewListLoading = true
        _uiState.value = _uiState.value.copy(
            isReviewListLoading = true,
            reviewListLoadingError = null
        )
        viewModelScope.launch {
            val reviewListResult = movieRepository.getMovieReviews(movieId, page)
            when (reviewListResult) {
                is Result.Success -> {
                    reviewListLastLoadedPage = page
                    _uiState.value = _uiState.value.copy(
                        reviewList = _uiState.value.reviewList + reviewListResult.data,
                        isReviewListLoading = false,
                        reviewListLoadingError = null
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isReviewListLoading = false,
                        reviewListLoadingError = reviewListResult
                    )
                }
            }
            isReviewListLoading = false
        }
    }

    fun cacheUserReview(rating: Int, reviewText: String) {
        _uiState.value = _uiState.value.copy(
            notSavedUserReviewRating = rating,
            notSavedUserReviewText = reviewText
        )
    }

    fun saveUserReview(rating: Int, reviewText: String) {
        val movieId = _uiState.value.movie?.id
        if (movieId == null) {
            _uiState.value = _uiState.value.copy(
                userReviewEditLoading = false,
                userReviewEditError = Result.Failure.Unknown,
                notSavedUserReviewRating = rating,
                notSavedUserReviewText = reviewText
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            userReviewEditLoading = true,
            userReviewEditError = null,
            notSavedUserReviewRating = rating,
            notSavedUserReviewText = reviewText
        )
        viewModelScope.launch {
            val saveReviewResult = movieRepository.saveReview(
                movieId = movieId,
                rating = rating,
                reviewText = reviewText
            )
            when (saveReviewResult) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        userReview = saveReviewResult.data,
                        userReviewEditLoading = false,
                        userReviewEditError = null,
                        notSavedUserReviewRating = null,
                        notSavedUserReviewText = null
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        userReviewEditLoading = false,
                        userReviewEditError = saveReviewResult,
                        notSavedUserReviewRating = rating,
                        notSavedUserReviewText = reviewText
                    )
                }
            }
        }
    }

    fun onDeleteReviewClicked() {
        val reviewId = _uiState.value.userReview?.id
        if (reviewId == null) {
            _uiState.value = _uiState.value.copy(
                userReviewEditLoading = false,
                userReviewEditError = Result.Failure.Unknown
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            userReviewEditLoading = true,
            userReviewEditError = null
        )
        viewModelScope.launch {
            val deleteReviewResult = movieRepository.deleteReview(
                reviewId = reviewId
            )
            when (deleteReviewResult) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        userReview = null,
                        userReviewEditLoading = false,
                        userReviewEditError = null
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        userReviewEditLoading = false,
                        userReviewEditError = deleteReviewResult
                    )
                }
            }
        }
    }

    fun onEditReviewClicked() {
        viewModelScope.launch {
            if (userRepository.isUserAuthorized()) {
                _showEditReviewDialogRequest.emit(Unit)
            } else {
                _showAuthDialogRequest.emit(Unit)
            }
        }
    }
}