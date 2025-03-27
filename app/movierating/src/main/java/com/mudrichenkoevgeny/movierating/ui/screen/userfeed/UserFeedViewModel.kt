package com.mudrichenkoevgeny.movierating.ui.screen.userfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.movierating.repository.movie.MovieRepository
import com.mudrichenkoevgeny.movierating.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFeedViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(UserFeedUiState())
    val uiState: StateFlow<UserFeedUiState> = _uiState

    private var lastLoadedPage: Int = 0
    private var isFeedLoading: Boolean = false

    init {
        loadFeed(pageNumber = 1)
    }

    fun loadFeed(pageNumber: Int? = null) {
        if (isFeedLoading) {
            return
        }
        val page = if (pageNumber == null) {
            lastLoadedPage + 1
        } else {
            pageNumber
        }
        isFeedLoading = true
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            movieListLoadingError = null
        )
        viewModelScope.launch {
            val feedResult = movieRepository.getUserFeed(page)
            when (feedResult) {
                is Result.Success -> {
                    lastLoadedPage = page
                    _uiState.value = _uiState.value.copy(
                        movieList = _uiState.value.movieList + feedResult.data,
                        isLoading = false,
                        movieListLoadingError = null
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        movieListLoadingError = feedResult
                    )
                }
            }
            isFeedLoading = false
        }
    }
}