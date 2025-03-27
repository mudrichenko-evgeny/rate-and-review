package com.mudrichenkoevgeny.movierating.ui.screen.base.contentlist

import com.mudrichenkoevgeny.movierating.result.Result

open class BaseContentListScreenUiState(
    open val movieList: List<Any> = emptyList(),
    open val isLoading: Boolean = false,
    open val movieListLoadingError: Result.Failure? = null
) {

    fun isShouldLoadNextPage(lastVisibleIndex: Int?): Boolean {
        return lastVisibleIndex != null &&
                lastVisibleIndex >= movieList.size - 1 &&
                !isLoading
                && movieListLoadingError == null
    }
}