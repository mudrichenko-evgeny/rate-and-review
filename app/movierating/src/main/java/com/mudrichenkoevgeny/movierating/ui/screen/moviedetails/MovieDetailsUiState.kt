package com.mudrichenkoevgeny.movierating.ui.screen.moviedetails

import com.mudrichenkoevgeny.core.model.domain.Review
import com.mudrichenkoevgeny.movierating.model.data.Movie
import com.mudrichenkoevgeny.movierating.result.Result

data class MovieDetailsUiState(
    val movie: Movie? = null,
    val isMovieLoading: Boolean = false,
    val movieLoadingError: Result.Failure? = null,
    val notSavedUserReviewRating: Int? = null,
    val notSavedUserReviewText: String? = null,
    val userReview: Review? = null,
    val userReviewEditLoading: Boolean = false,
    val userReviewEditError: Result.Failure? = null,
    val reviewList: List<Review> = emptyList(),
    val isReviewListLoading: Boolean = false,
    val reviewListLoadingError: Result.Failure? = null
) {

    fun isShouldLoadNextPage(lastVisibleIndex: Int?): Boolean {
        return lastVisibleIndex != null &&
                lastVisibleIndex >= reviewList.size - 1 &&
                !isReviewListLoading
                && reviewListLoadingError == null
    }

    fun getEditReviewRating(): Int? {
        return userReview?.rating ?: notSavedUserReviewRating
    }

    fun getEditReviewText(): String {
        return userReview?.reviewText ?: notSavedUserReviewText ?: ""
    }
}