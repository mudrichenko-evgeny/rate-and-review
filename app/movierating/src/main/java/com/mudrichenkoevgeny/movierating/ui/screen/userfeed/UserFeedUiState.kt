package com.mudrichenkoevgeny.movierating.ui.screen.userfeed

import com.mudrichenkoevgeny.movierating.model.data.Movie
import com.mudrichenkoevgeny.movierating.result.Result
import com.mudrichenkoevgeny.movierating.ui.screen.base.contentlist.BaseContentListScreenUiState

data class UserFeedUiState(
    override val movieList: List<Movie> = emptyList(),
    override val isLoading: Boolean = false,
    override val movieListLoadingError: Result.Failure? = null
) : BaseContentListScreenUiState()